/*
 * The MIT License
 *
 * Copyright 2017 Universidad De Los Andes - Departamento de Ingeniería de Sistemas.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.edu.uniandes.isis2503.nosqljpa.service;

import co.edu.uniandes.isis2503.nosqljpa.auth.AuthorizationFilter;
import co.edu.uniandes.isis2503.nosqljpa.auth.AuthorizationFilter.Role;
import co.edu.uniandes.isis2503.nosqljpa.auth.Secured;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IAlarmaConverter;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IAlarmaLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.ConjuntoLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.InmuebleLogic;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.ConjuntoDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.InmuebleDTO;
import com.sun.istack.logging.Logger;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IInmuebleLogic;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IConjuntoLogic;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IHubLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.AlarmaLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.HubLogic;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaDTO2;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.ConjuntoDTO2;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.InmuebleDTO2;
import java.util.ArrayList;

/**
 *
 * @author ca.mendoza968
 */
@Path("/conjuntos")
@Secured({Role.administrador,Role.yale,Role.seguridad})
@Produces(MediaType.APPLICATION_JSON)
public class ConjuntoService {

    private final IConjuntoLogic conjuntoLogic;
    private final IInmuebleLogic inmuebleLogic;
    private final IAlarmaLogic alarmaLogic;
    private final IHubLogic hubLogic;

    public ConjuntoService() {
        this.conjuntoLogic = new ConjuntoLogic();
        this.inmuebleLogic = new InmuebleLogic();
        this.alarmaLogic = new AlarmaLogic();
        this.hubLogic = new HubLogic();
    }

    @POST
    public ConjuntoDTO add(ConjuntoDTO2 dto) {
        for(InmuebleDTO2 i: dto.getInmuebles()){
            for(AlarmaDTO a: i.getAlarmas()){
                 alarmaLogic.add(a);
            }
            if(i.getHub()!=null){
                hubLogic.add(i.getHub());
            }
            inmuebleLogic.add(i.convert());
        }
        return conjuntoLogic.add(dto.convert());
    }
    
    @POST
    @Path("{id}/inmuebles")
    public InmuebleDTO addInmueble(@PathParam("id") String id, InmuebleDTO dto) {
        ConjuntoDTO conjunto = conjuntoLogic.find(id);
        InmuebleDTO result = inmuebleLogic.add(dto);
        conjunto.addInmueble(dto.getId());
        conjuntoLogic.update(conjunto);
        return result;
    }

    @PUT
    public ConjuntoDTO update(ConjuntoDTO dto) {
        return conjuntoLogic.update(dto);
    }

    @GET
    @Path("/{id}")
    @Secured((Role.yale))
    public ConjuntoDTO find(@PathParam("id") String id) {
        return conjuntoLogic.find(id);
    }
    
    @GET
    @Path("/{id}/alarmas")
    public List<AlarmaDTO2> findAlarmas(@PathParam("id") String id) {
       List<AlarmaDTO2> alarmas=new ArrayList<>();
       ConjuntoDTO c=conjuntoLogic.find(id);
            for(String i:c.getInmuebles()){
                InmuebleDTO iAct=inmuebleLogic.find(i);
                for(String a:iAct.getAlarmas()){
                    AlarmaDTO aAct=alarmaLogic.find(a);
                    AlarmaDTO2 alarma=new AlarmaDTO2(aAct.getId(),aAct.getTipo(), iAct.getId(), c.getId());
                    alarmas.add(alarma);
                }
            }
        
        return alarmas;
    }
    
    @GET
    @Path("/{id}/inmuebles")
    public List<InmuebleDTO> findInmuebles(@PathParam("id") String id) {
        ConjuntoDTO conjunto;
        List<InmuebleDTO> inmuebles=new ArrayList<>();
        try{
        conjunto=conjuntoLogic.find(id);
        for(String i:conjunto.getInmuebles()){
            inmuebles.add(inmuebleLogic.find(i));
        }
        }catch(Exception e){
        }
        return inmuebles;
    }
    
    @GET
    @Path("/alarmas")
    @Secured({Role.yale})
    public List<AlarmaDTO2> findAlarmas() {
        List<AlarmaDTO2> alarmas=new ArrayList<>();
        for(ConjuntoDTO c:conjuntoLogic.all()){
            for(String i:c.getInmuebles()){
                InmuebleDTO iAct=inmuebleLogic.find(i);
                for(String a:iAct.getAlarmas()){
                    AlarmaDTO aAct=alarmaLogic.find(a);
                    AlarmaDTO2 alarma=new AlarmaDTO2(aAct.getId(),aAct.getTipo(), iAct.getId(), c.getId());
                    alarmas.add(alarma);
                }
            }
        }
        return alarmas;
    }

    @GET
    @Secured({Role.yale})
    public List<ConjuntoDTO> all() {
        return conjuntoLogic.all();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            conjuntoLogic.delete(id);
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Floor was deleted").build();
        } catch (Exception e) {
            Logger.getLogger(ConjuntoService.class).log(Level.WARNING, e.getMessage());
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
        }
    }
}
