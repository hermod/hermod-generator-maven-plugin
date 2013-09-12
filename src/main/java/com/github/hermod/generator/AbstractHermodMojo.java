package com.github.hermod.generator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import com.github.hermod.generator.impl.AnnotatedClassParser;
import com.github.hermod.generator.impl.ClassContainerDescriptorValidator;
import com.github.hermod.generator.impl.MustacheGenerator;
import com.github.hermod.generator.model.ClassContainerDescriptor;

/**
 * <p>
 * HermodGeneratorMojo.
 * </p>
 * 
 * @author anavarro - Mar 20, 2013
 * 
 */
public abstract class AbstractHermodMojo
    extends AbstractMojo
{
    @Parameter(property = "hermod.skip", defaultValue = "false")
    protected boolean      skip;
    
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject project;
    
    protected final void configurePluginClasspath() throws MojoExecutionException
    
    {
        
        // if we are configured to include the provided dependencies on the plugin's classpath
        // (which mimics being on jetty's classpath vs being on the webapp's classpath), we first
        // try and filter out ones that will clash with jars that are plugin dependencies, then
        // create a new classloader that we setup in the parent chain.
        try
        {
            List<URL> provided = new ArrayList<URL>();
            URL[] urls = null;
            
            for (Object oArtifact : project.getDependencyArtifacts())
            {
                Artifact artifact = (Artifact) oArtifact;
                // if (Artifact.SCOPE_PROVIDED.equals(artifact.getScope()))// && !isPluginArtifact(artifact))
                // {
                try
                {
                    
                    provided.add(artifact.getFile().toURI().toURL());
                    if (getLog().isDebugEnabled())
                    {
                        getLog().debug("Adding provided artifact: " + artifact);
                    }
                    // }
                }
                catch (Exception aException)
                {
                    getLog().warn("Faield to add " + artifact + " to plugin classapth");
                }
                
            }
            
            provided.add(new File(project.getBuild().getOutputDirectory()).toURI().toURL());
            if (!provided.isEmpty())
            {
                urls = new URL[provided.size()];
                provided.toArray(urls);
                URLClassLoader loader = new URLClassLoader(urls, getClass().getClassLoader());
                Thread.currentThread().setContextClassLoader(loader);
                getLog().info("Plugin classpath augmented with <scope>provided</scope> dependencies: " + Arrays.toString(urls));
            }
        }
        catch (MalformedURLException e)
        {
            throw new MojoExecutionException("Invalid url", e);
        }
    }
    
}
