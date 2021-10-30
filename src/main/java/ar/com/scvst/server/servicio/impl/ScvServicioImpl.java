package ar.com.scvst.server.servicio.impl;

import ar.com.scvst.server.bean.CambioArchivo;
import ar.com.scvst.server.bean.Commit;
import ar.com.scvst.server.bean.Configuracion;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;
import org.springframework.stereotype.Service;
import ar.com.scvst.server.servicio.ScvServicio;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.StreamSupport;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.zeroturnaround.zip.ZipUtil;

/**
 *
 * @author Tomás Serra <tomas@serra.com.ar>
 */
@Service("scvServicio")
public class ScvServicioImpl implements ScvServicio {

    private static final Logger LOG = Logger.getLogger(ScvServicioImpl.class.getName());

    public static void main(String[] args) throws IOException, GitAPIException {
        String direccionLocal = "/Users/tserra/Documents/Tesis/scvst";
        String direccionRemota = "git@gitlab.com:stecno/scvst.git";
        String usuario = "tserra";
        String clave = "12Gitlab12";
        ScvServicioImpl srv = new ScvServicioImpl();
        Configuracion configuracion = srv.iniciar(
                direccionLocal,
                direccionRemota,
                false,
                usuario, clave);

    }

    @Override
    public void clonarRepositorio(Configuracion configuracion) {
        try {
            CloneCommand command = Git.cloneRepository()
                    .setURI(configuracion.getUbicacionRemota())
                    .setCloneAllBranches(true)
                    .setDirectory(new File(configuracion.getUbicacionLocal()));
            if (configuracion.getCredenciales() != null) {
                command.setCredentialsProvider(configuracion.getCredenciales());
            }
            command.call();
        } catch (GitAPIException ex) {
            LOG.log(Level.SEVERE, "Error al clonar el repositorio", ex);
        }
    }

    @Override
    public void commit(Configuracion configuracion, List<String> archivos, String mensaje) {
        try {
            if (CollectionUtils.isNotEmpty(archivos)) {
                AddCommand aC = configuracion
                        .getGit()
                        .add();
                archivos.forEach(a -> aC.addFilepattern(a));
                aC.call();
            }
            configuracion
                    .getGit()
                    .commit()
                    .setMessage(mensaje)
                    .call();
        } catch (GitAPIException ex) {
            LOG.log(Level.SEVERE, "Error al realizar el commit", ex);
        }
    }

    @Override
    public void push(Configuracion configuracion) {
        PushCommand pc = configuracion.getGit().push();
        if (configuracion.getCredenciales() != null) {
            pc.setCredentialsProvider(configuracion.getCredenciales());
        }
        pc.setForce(true)
                .setPushAll();
        try {
            Iterator<PushResult> it = pc.call().iterator();
            if (it.hasNext()) {
                System.out.println(it.next().toString());
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se pudo realizar el push", ex);
        }
    }

    @Override
    public List<String> pull(Configuracion configuracion) {
        List<String> res = null;
        try {
            PullCommand pC = configuracion
                    .getGit()
                    .pull();
            if (configuracion.getCredenciales() != null) {
                pC.setCredentialsProvider(configuracion.getCredenciales());
            }
            PullResult pRes = pC.call();
            if (pRes.isSuccessful()) {
            }
        } catch (GitAPIException ex) {
            LOG.log(Level.SEVERE, "No se pudo realizar el pull", ex);
        }
        return res;
    }

    @Override
    public List<CambioArchivo> obtenerArchivosModificados(Configuracion configuracion) {
        List<CambioArchivo> res = null;
        try {

            Status estado = configuracion
                    .getGit()
                    .status()
                    .call();
            if (!estado.isClean()) {
                res = new ArrayList();
                res.addAll(estado.getModified().stream().map(nombre -> new CambioArchivo(nombre, CambioArchivo.Estado.MODIFICADO))
                        .collect(Collectors.toList()));
                res.addAll(estado.getChanged().stream().map(nombre -> new CambioArchivo(nombre, CambioArchivo.Estado.AGREGADO))
                        .collect(Collectors.toList()));
                res.addAll(estado.getAdded().stream().map(nombre -> new CambioArchivo(nombre, CambioArchivo.Estado.AGREGADO))
                        .collect(Collectors.toList()));
                res.addAll(estado.getUntracked().stream().map(nombre -> new CambioArchivo(nombre, CambioArchivo.Estado.MODIFICADO))
                        .collect(Collectors.toList()));
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se pudo recuperar la lista de los archivos modificados o agregados", ex);
        }

        return res;
    }

    public List<String> obtenerStashs(Configuracion configuracion) {
        List<String> res = null;
        try {
            res = configuracion
                    .getGit()
                    .stashList()
                    .call()
                    .stream()
                    .map(s -> s.getName())
                    .collect(Collectors.toList());
        } catch (GitAPIException ex) {
            LOG.log(Level.SEVERE, "No se pudo obtener la lista de stash", ex);
        }
        return res;
    }

    public void realizarStash(Configuracion configuracion, String mensaje) {
        try {
            configuracion
                    .getGit()
                    .stashCreate()
                    .setIndexMessage(mensaje)
                    .call();
        } catch (GitAPIException ex) {
            LOG.log(Level.SEVERE, "No se pudo realizar el stash", ex);
        }
    }

    public void cargarStash(Configuracion configuracion, String idStash) {
        try {
            configuracion
                    .getGit()
                    .stashApply()
                    .setStashRef(idStash)
                    .call();
        } catch (GitAPIException ex) {
            LOG.log(Level.SEVERE, "No se pudo aplicar el stash", ex);
        }
    }

    @Override
    public Configuracion iniciar(String direccionLocal, String repositorio, boolean repositorioEsNuevo, String usuario, String clave) {
        Configuracion configuracion = null;
        try {
            if (repositorioEsNuevo) {
                Git gitRemote = Git.init().setDirectory(new File(repositorio)).setBare(true).call();
            }

            configuracion = new Configuracion(direccionLocal, repositorio, usuario, clave, null);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "No se puede iniciar", ex);
        }
        return configuracion;
    }

    @Override
    public List<Commit> obtenerHistorialCommits(Configuracion configuracion) {
        List<Commit> res = null;
        try {
            Iterable<RevCommit> commits = configuracion.getGit()
                    .log().add(configuracion.getRepositorioLocal()
                            .resolve("refs/heads/develop"))
                    .call();
            if (commits != null) {
                res = StreamSupport.stream(commits.spliterator(), false)
                        .collect(Collectors.toList()).stream().map(c -> new Commit(
                        c.getId().getName(),
                        LocalDateTime.ofInstant(c.getAuthorIdent().getWhen().toInstant(),
                                ZoneId.systemDefault()),
                        c.getAuthorIdent().getName(),
                        c.getFullMessage()
                )).collect(Collectors.toList());
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al obtener el hostrial de commits", ex);
        }

        return res;
    }

    @Override
    public String obtenerCambiosCommit(Configuracion configuracion, String hashID) {
        String res = null;
        try {
            RevCommit commitNuevo;
            try ( RevWalk walk = new RevWalk(configuracion.getRepositorioLocal())) {
                commitNuevo = walk.parseCommit(configuracion.getRepositorioLocal().resolve(hashID));
            }

            res = obtenerDiferenciaCommit(commitNuevo, configuracion.getGit(), configuracion.getRepositorioLocal());
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al obtener cambios de commit", ex);
        }
        return res;

    }

    private String obtenerDiferenciaCommit(RevCommit newCommit, Git git, Repository repo) throws IOException {

        //Get commit that is previous to the current one.
        RevCommit oldCommit = getPrevHash(newCommit, git, repo);
        if (oldCommit == null) {
            return "Start of repo";
        }
        //Use treeIterator to diff.
        AbstractTreeIterator oldTreeIterator = getCanonicalTreeParser(oldCommit, git, repo);
        AbstractTreeIterator newTreeIterator = getCanonicalTreeParser(newCommit, git, repo);
        OutputStream outputStream = new ByteArrayOutputStream();
        try ( DiffFormatter formatter = new DiffFormatter(outputStream)) {
            formatter.setRepository(git.getRepository());
            formatter.format(oldTreeIterator, newTreeIterator);
        }
        String diff = outputStream.toString();
        return diff;
    }
//Helper function to get the previous commit.

    public RevCommit getPrevHash(RevCommit commit, Git git, Repository repo) throws IOException {

        try ( RevWalk walk = new RevWalk(repo)) {
            // Starting point
            walk.markStart(commit);
            int count = 0;
            for (RevCommit rev : walk) {
                // got the previous commit.
                if (count == 1) {
                    return rev;
                }
                count++;
            }
            walk.dispose();
        }
        //Reached end and no previous commits.
        return null;
    }
//Helper function to get the tree of the changes in a commit. Written by Rüdiger Herrmann

    private AbstractTreeIterator getCanonicalTreeParser(ObjectId commitId, Git git, Repository repo) throws IOException {
        try ( RevWalk walk = new RevWalk(git.getRepository())) {
            RevCommit commit = walk.parseCommit(commitId);
            ObjectId treeId = commit.getTree().getId();
            try ( ObjectReader reader = git.getRepository().newObjectReader()) {
                return new CanonicalTreeParser(null, reader, treeId);
            }
        }
    }
    
}
