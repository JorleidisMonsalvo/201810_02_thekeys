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

import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.ClaveDTO;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import kafka.KafkaProductor;
/**
 *
 * @author ca.mendoza968
 */
@Path("/clave")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClavesService {

    public static final int TAMAÑOCLAVE = 4;          
    public static KafkaProductor kafka;
    
    public ClavesService() {
        kafka = new KafkaProductor();
    }
    @POST
    @Path("/Update/{conjunto}/{apto}/{contraAnterior}/{nuevaContra}")
    public Response update(@PathParam("conjunto") String conjunto,@PathParam("apto") String apto,
                             @PathParam("contraAnterior") String contraAnterior, @PathParam("nuevaContra") String nuevaContra ){
        if(contraAnterior.length()!=TAMAÑOCLAVE || nuevaContra.length()!=TAMAÑOCLAVE)
        {
            return Response.status(400).entity(doErrorMessage("las contraseñas deben tener 4 digitos")).build();
        }
        ClaveDTO clave =new ClaveDTO(conjunto, apto, nuevaContra, contraAnterior,"UPDATE");
        kafka.publicar(clave);
        return Response.status(200).entity(clave).build();
    }
    @POST
    @Path("/Delete/{conjunto}/{apto}/{contraseña}")
    public Response delete(@PathParam("conjunto") String conjunto,@PathParam("apto") String apto,
                             @PathParam("contraseña") String contraseña ) {
        if(contraseña.length()!=TAMAÑOCLAVE )
        {
            return Response.status(400).entity(doErrorMessage("las contraseñas deben tener 4 digitos")).build();
        }
        ClaveDTO clave= new ClaveDTO(conjunto, apto, "", contraseña,"DELETE");
        kafka.publicar(clave);
        return Response.status(200).entity(clave).build();
    }
    @POST
    @Path("/Create/{conjunto}/{apto}/{contraseña}")
    public Response create(@PathParam("conjunto") String conjunto,@PathParam("apto") String apto,
                             @PathParam("contraseña") String contraseña ){
        if(contraseña.length()!=TAMAÑOCLAVE )
        {
            return Response.status(400).entity(doErrorMessage("las contraseñas deben tener 4 digitos")).build();
        }
        ClaveDTO clave = new ClaveDTO(conjunto, apto, "", contraseña,"CREATE");
        kafka.publicar(clave);
       return Response.status(200).entity(new ClaveDTO(conjunto, apto, "", contraseña,"CREATE")).build();
    }
    @POST
    @Path("/Get/{conjunto}/{apto}")
    public ClaveDTO getAll(@PathParam("conjunto") String conjunto,@PathParam("apto") String apto) {
        ClaveDTO clave = new ClaveDTO(conjunto, apto, "", "","GETALL");
        kafka.publicar(clave);
        return clave;
    }
    @POST
    @Path("/Get/{conjunto}/{apto}/{contraseña}")
    public Response get(@PathParam("conjunto") String conjunto,@PathParam("apto") String apto,
                            @PathParam("contraseña") String contraseña){
        if(contraseña.length()!=TAMAÑOCLAVE )
        {
           return Response.status(400).entity(doErrorMessage("las contraseñas deben tener 4 digitos")).build();
        }
        ClaveDTO clave = new ClaveDTO(conjunto, apto, "", contraseña,"GET");
        kafka.publicar(clave);
        return Response.status(200).entity(clave).build();
    }
    public String doErrorMessage(String e){
		return "{ \"ERROR\": \""+ e + "\"}" ;
	}
}
