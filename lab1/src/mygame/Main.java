package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

import com.jme3.texture.Texture;
import java.lang.Math;


public class Main extends SimpleApplication {
    
  public static void main(String args[]) {
    Main app = new Main();
    app.start();
  }
  
  private BulletAppState bulletAppState;
  private static final Sphere dot;
  
  JupiterPhysics jupiterPhy;
  VoyagerPhysics voyagerPhy;

  private int mult = 20;
  private boolean checker;
 
  static {
    dot = new Sphere(32,32,0.1f);
  }
  
  double gravity_assist_func(double x){
    return Math.sqrt(16f*(x+15f)*(x+10f)/28f - 16f);
  }
    
  @Override
  public void simpleInitApp() {
    checker = true;
    
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    
    cam.setLocation(new Vector3f(52f, 0f, 6f));
    cam.lookAt(new Vector3f(2, 8f, 13f), Vector3f.UNIT_Y);
    
    initModels();
  }

 
  public void initModels() {
    Material jupiter_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    jupiter_mat.setColor("Color", ColorRGBA.Brown);
    jupiterPhy = new JupiterPhysics(jupiter_mat);
    bulletAppState.getPhysicsSpace().add(voyagerPhy.voyagerRBC);
    rootNode.attachChild(voyagerPhy.voyagerGeo);
    
    Material voyager_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
    key2.setGenerateMips(true);
    Texture tex2 = assetManager.loadTexture(key2);
    voyager_mat.setTexture("ColorMap", tex2);
    voyagerPhy = new VoyagerPhysics(voyager_mat);
    bulletAppState.getPhysicsSpace().add(jupiterPhy.jupiterRBC);
    rootNode.attachChild(jupiterPhy.jupiterGeo);
    
    initGraf();
  }

  public void initGraf(){
      float x0 = 30f;
      Material dot_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
      dot_mat.setColor("Color", ColorRGBA.Red);
      while(x0 != -7f){
          float y = (float)gravity_assist_func(x0);
          Geometry dot_geo1 = new Geometry("dot", dot);
          dot_geo1.setLocalTranslation(new Vector3f(0,y,x0));
          Geometry dot_geo2 = new Geometry("dot", dot);
          dot_geo2.setLocalTranslation(new Vector3f(0,-y,x0));
          dot_geo1.setMaterial(dot_mat);
          dot_geo2.setMaterial(dot_mat);
          rootNode.attachChild(dot_geo1);
          rootNode.attachChild(dot_geo2);
          x0 -= 0.5f;
      }
      Geometry dot_geo1 = new Geometry("dot", dot);
      dot_geo1.setLocalTranslation(new Vector3f(0,0,x0+0.4f));
      dot_geo1.setMaterial(dot_mat);
      rootNode.attachChild(dot_geo1);
  }
  
  
  @Override 
  public void simpleUpdate(float tpf){
      float x = checker ? voyagerPhy.voyagerPos.z-0.2f :voyagerPhy.voyagerPos.z+0.2f;
      if(x < -6.6f){
          checker = false;
          x = -6.6f;
          mult += 5;
      }
      else{
          mult += 1;
      }
      
      int mul = checker ? 1 : -1;
      float y = mul*(float)gravity_assist_func(x);
      double newVForce = voyagerPhy.force(y);
      double l = voyagerPhy.distance(jupiterPhy.jupiterPos);
      double newGForce = jupiterPhy.force(voyagerPhy.M, l);
      
      Vector3f gForce = new Vector3f(0,(float)(-voyagerPhy.voyagerPos.y*newGForce/l),(float)(-voyagerPhy.voyagerPos.z*newGForce/l));
      Vector3f vForce = new Vector3f(0, (float)((y-voyagerPhy.voyagerPos.y)/newVForce-gForce.y), (float)((x -voyagerPhy.voyagerPos.z)/newVForce-gForce.z));
      Vector3f absForce = new Vector3f(0, gForce.y + vForce.y,gForce.z + vForce.z);
     
      voyagerPhy.voyagerRBC.setLinearVelocity(absForce.mult(mult/20));
  }
}