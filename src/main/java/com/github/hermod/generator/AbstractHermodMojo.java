package com.github.hermod.generator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

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
    
    @Parameter(property = "modelInput", required = false)
    protected String       modelInput;
    
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
                    
                    provided.add(artifact.getFile()
                                         .toURI()
                                         .toURL());
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
            
            provided.add(new File(project.getBuild()
                                         .getOutputDirectory()).toURI()
                                                               .toURL());
            if (!provided.isEmpty())
            {
                urls = new URL[provided.size()];
                provided.toArray(urls);
                URLClassLoader loader = new URLClassLoader(urls, getClass().getClassLoader());
                Thread.currentThread()
                      .setContextClassLoader(loader);
                getLog().info("Plugin classpath augmented with <scope>provided</scope> dependencies: " + Arrays.toString(urls));
            }
        }
        catch (MalformedURLException e)
        {
            throw new MojoExecutionException("Invalid url", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    protected final Map<String, Object> updateMustachScope() throws ClassNotFoundException, MojoExecutionException, InstantiationException, IllegalAccessException
    {
        final Map<String, Object> values = new HashMap<>();
        if (modelInput != null)
        {
            final Class<?> clazz = Class.forName(modelInput, true, Thread.currentThread()
                                                                         .getContextClassLoader());
            if (!Map.class.isAssignableFrom(clazz))
                throw new MojoExecutionException(modelInput + " doesn't implement Map<String, Object>");

            values.putAll(((Map<String, Object>) clazz.newInstance()));
        }

        return values;
    }
    
}
