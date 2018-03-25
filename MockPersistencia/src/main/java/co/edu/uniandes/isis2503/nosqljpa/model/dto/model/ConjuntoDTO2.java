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
package co.edu.uniandes.isis2503.nosqljpa.model.dto.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ca.mendoza968
 */
@XmlRootElement
public class ConjuntoDTO2 {
    private String id;
    private String nombre;
    private String direccion;
    private List<InmuebleDTO2> inmuebles;

    public List<InmuebleDTO2> getInmuebles() {
        return inmuebles;
    }

    public void setInmuebles(List<InmuebleDTO2> inmuebles) {
        this.inmuebles = inmuebles;
    }

    public ConjuntoDTO2() {
        this.inmuebles = new ArrayList();
    }

    public ConjuntoDTO2(String id, String name, String direccion,List<InmuebleDTO2> inmuebles) {
        this.id = id;
        this.nombre = name;
        this.direccion = direccion;
        this.inmuebles = inmuebles;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public ConjuntoDTO convert(){
        ConjuntoDTO c= new ConjuntoDTO();
        c.setId(this.id);
        c.setDireccion(this.direccion);
        c.setNombre(this.nombre);
        c.setInmuebles(new ArrayList<String>());
        for(InmuebleDTO2 i:this.inmuebles){
            c.addInmueble(i.getId());
        }
        return c;
    }

}
