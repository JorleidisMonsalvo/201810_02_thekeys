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

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ca.mendoza968
 */
@XmlRootElement
public class ClaveDTO {
    private String conjunto;
    private String apto;
    private String nuevaContra;
    private String contraseñaActual;
    private String asunto;

    public ClaveDTO(String conjunto, String apto, String nuevaContra, String contraAnterior, String asunto) {
        this.conjunto = conjunto;
        this.apto = apto;
        this.nuevaContra = nuevaContra;
        this.contraseñaActual = contraAnterior;
        this.asunto = asunto;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }
    
    public String getConjunto() {
        return conjunto;
    }

    public void setConjunto(String conjunto) {
        this.conjunto = conjunto;
    }

    public String getApto() {
        return apto;
    }

    public void setApto(String apto) {
        this.apto = apto;
    }

    public String getNuevaContra() {
        return nuevaContra;
    }

    public void setNuevaContra(String nuevaContra) {
        this.nuevaContra = nuevaContra;
    }

    public String getContraAnterior() {
        return contraseñaActual;
    }

    public void setContraAnterior(String contraAnterior) {
        this.contraseñaActual = contraAnterior;
    }
    
    

    
}
