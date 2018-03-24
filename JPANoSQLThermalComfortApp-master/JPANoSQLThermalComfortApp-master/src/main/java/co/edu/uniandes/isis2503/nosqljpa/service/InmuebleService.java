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

import co.edu.uniandes.isis2503.nosqljpa.interfaces.IConsolidatedDataLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.InmuebleLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.ConsolidatedDataLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.AlarmaLogic;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.InmuebleDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.ConsolidatedDataDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaDTO;
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
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IAlarmaLogic;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IInmuebleLogic;

/**
 *
 * @author ca.mendoza968
 */
@Path("/inmuebles")
@Produces(MediaType.APPLICATION_JSON)
public class InmuebleService {
    private final IInmuebleLogic inmuebleLogic;
    private final IAlarmaLogic alarmaLogic;

    public InmuebleService() {
        this.inmuebleLogic = new InmuebleLogic();
        this.alarmaLogic = new AlarmaLogic();
    }

    @POST
    public InmuebleDTO add(InmuebleDTO dto) {
        return inmuebleLogic.add(dto);
    }
    
    
    @POST
    @Path("{id}/alarmas")
    public AlarmaDTO addAlarma(@PathParam("id") String id, AlarmaDTO dto) {
        InmuebleDTO inmueble = inmuebleLogic.find(id);
        AlarmaDTO result = alarmaLogic.add(dto);
        inmueble.addAlarma(dto.getId());
        inmuebleLogic.update(inmueble);
        return result;
    }

    @PUT
    public InmuebleDTO update(InmuebleDTO dto) {
        return inmuebleLogic.update(dto);
    }

    @GET
    @Path("/{id}")
    public InmuebleDTO find(@PathParam("id") String id) {
        return inmuebleLogic.find(id);
    }

    @GET
    public List<InmuebleDTO> all() {
        return inmuebleLogic.all();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            inmuebleLogic.delete(id);
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Room was deleted").build();
        } catch (Exception e) {
            Logger.getLogger(InmuebleService.class).log(Level.WARNING, e.getMessage());
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
        }
    }    
}