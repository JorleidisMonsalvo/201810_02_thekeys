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
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.ConjuntoDTO2;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.InmuebleDTO2;

/**
 *
 * @author ca.mendoza968
 */
@Path("/conjuntos")
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
    @Path("{code}/rooms")
    public InmuebleDTO addRoom(@PathParam("code") String code, InmuebleDTO dto) {
        ConjuntoDTO floor = conjuntoLogic.findCode(code);
        InmuebleDTO result = inmuebleLogic.add(dto);
//        floor.addRoom(dto.getId());
        conjuntoLogic.update(floor);
        return result;
    }

    @PUT
    public ConjuntoDTO update(ConjuntoDTO dto) {
        return conjuntoLogic.update(dto);
    }

    @GET
    @Path("/{id}")
    public ConjuntoDTO find(@PathParam("id") String id) {
        return conjuntoLogic.find(id);
    }

    @GET
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
