/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;


public class VoyagerPhysics {
    public final double M = 1898e14;
    public Geometry voyagerGeo;
    public RigidBodyControl voyagerRBC;
    public Vector3f voyagerPos;
    
    VoyagerPhysics(Material voyager_mat){
      voyagerGeo = new Geometry("Voyager", new Sphere(32, 32, 0.2f));
      voyagerGeo.setMaterial(voyager_mat);
      
      voyagerRBC = new RigidBodyControl((float)M);
      voyagerPos = new Vector3f(0,32f,30f);
      voyagerGeo.setLocalTranslation(voyagerPos);
      voyagerGeo.addControl(voyagerRBC);
      voyagerRBC.setGravity(Vector3f.ZERO);
    }
    
    double distance(Vector3f jupiterPos){
        voyagerPos = voyagerGeo.getLocalTranslation();
        return Math.sqrt(Math.pow(voyagerPos.y - jupiterPos.y, 2) + Math.pow(voyagerPos.z - jupiterPos.z, 2));
    }
    
    double force(double y){
        return Math.sqrt(Math.pow(y-voyagerPos.y, 2) + Math.pow(1f, 2));
    }
}
