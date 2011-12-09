package org.openjpa.ide.idea.integration;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.LinkedList;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ProjectRootsTraversing;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PathsList;
import com.intellij.util.lang.UrlClassLoader;

/**
 * Factory for creating ClassLoaders restricted to each provided module's dependency scope.
 */
public abstract class ClassLoaderFactory {

    private ClassLoaderFactory() {
        // prohibit instantiation
    }

    /**
     * Creates a new {@link ClassLoader} that includes only the dependencies
     * and output dirs in the current module's compile context (includes module dependencies
     * and external jar dependencies).
     *
     * @param compileContext .
     * @param module         .
     * @param proxyClass     the class of the proxy instantiating a ClassLoader
     * @return .
     * @throws java.io.IOException .
     */
    @SuppressWarnings("deprecation") // want to stay backwards compatible at any cost
    public static ClassLoader newClassLoader(final CompileContext compileContext, final Module module, final Class<?> proxyClass) throws IOException {
        final Collection<URL> urls = new LinkedList<URL>();

        // get urls from actual class loader to be able to instantiate executors
        final UrlClassLoader loader = (UrlClassLoader) (proxyClass == null ? ClassLoaderFactory.class.getClassLoader() : proxyClass.getClassLoader());
        urls.addAll(loader.getUrls());

        for (final VirtualFile vf : compileContext.getAllOutputDirectories()) {
            final File file = new File(vf.getPath());
            final File canonicalFile = file.getCanonicalFile();
            final URI uri = canonicalFile.toURI();
            final URL url = uri.toURL();
            urls.add(url);
        }

        final PathsList paths = ProjectRootsTraversing.collectRoots(module, ProjectRootsTraversing.PROJECT_LIBRARIES);

        for (final VirtualFile vf : paths.getVirtualFiles()) {
            final File f = new File(vf.getPath());
            final URI uri = f.toURI();
            final URL url = uri.toURL();
            urls.add(url);
        }

        return new URLClassLoader(urls.toArray(new URL[urls.size()]));
    }

}
