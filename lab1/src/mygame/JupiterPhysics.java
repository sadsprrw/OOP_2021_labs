/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author jekav
 */
public class JupiterPhysics {
    
    public final double M = 1898e14;
    public Geometry jupiterGeo;
    public RigidBodyControl jupiterRBC;
    public final Vector3f jupiterPos;
    
    JupiterPhysics(Material jupiterMat){
      jupiterGeo = new Geometry("Jupiter", new Sphere(32, 32, 4f));
      jupiterGeo.setMaterial(jupiterMat);
      
      jupiterRBC = new RigidBodyControl((float)M);
      jupiterPos = new Vector3f(0, 0, 0);
      jupiterGeo.addControl(jupiterRBC);
      jupiterRBC.setGravity(Vector3f.ZERO);
    }
    
    double force(double m1, double distance)
    {
        double G;
        G = 0.00000000667;
        double F;
        F = (G * m1 * M) / (distance * distance);

        return F;
    }
}
