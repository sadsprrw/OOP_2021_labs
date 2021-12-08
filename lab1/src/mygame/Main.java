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
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import java.lang.Math;


public class Main extends SimpleApplication {
    
  public static void main(String args[]) {
    Main app = new Main();
    app.start();
  }
  
  private BulletAppState bulletAppState;
  
  private RigidBodyControl    voyg_phy;
  private static final Sphere sphere;
  private static final Sphere jupiter;
  private static final Sphere dot;
   
  private RigidBodyControl    jupiter_phy;
  
  Material jupiter_mat;
  Material stone_mat;
  
  private int mult = 20;
  
  private Vector3f vForce;
  private final Vector3f jPos = new Vector3f(0,0,0);
  private Geometry voyg_geo;
  private Geometry jupiter_geo;
  
  private Vector3f vPos;
  
  private boolean checker;
  
  private final double voyg_m = 7219e-10;
  private final double jupiter_m = 1898e14;
  private double l0 = 42f; 
  static {
    jupiter = new Sphere(32, 32, 4f);
    
    sphere = new Sphere(32, 32, 0.2f);
    
    dot = new Sphere(32,32,0.1f);
  }
  
  
  
    double force()
      {
          double G;
          G = 0.00000000667;
          double F;
          F = (G * voyg_m * jupiter_m) / (l0 * l0);

          // Rounding to two digits after decimal
          return F;
      }
 
    void distance(Vector3f vPos){
        l0 = Math.sqrt(Math.pow(vPos.y, 2) + Math.pow(vPos.z, 2));
    }
    
    double gravity_assist_func(double x){
        return Math.sqrt(16f*(x+15f)*(x+10f)/28f - 16f);
    }
    
    double vForce(double y){
        return Math.sqrt(Math.pow(y-vPos.y, 2) + Math.pow(1f, 2));
    }
    
  @Override
  public void simpleInitApp() {
    checker = true;
    vForce = new Vector3f(0,0.1f,0.1f);  
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    
    cam.setLocation(new Vector3f(52f, 0f, 6f));
    cam.lookAt(new Vector3f(2, 8f, 13f), Vector3f.UNIT_Y);
    
    
    inputManager.addMapping("shoot",
            new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addListener(actionListener, "shoot");
    
    initMaterials();
    initVoyager();
    initJupiter();
    initGraf();
    
     
  }
  
    private ActionListener actionListener = new ActionListener() {
     public void onAction(String name, boolean keyPressed, float tpf) {
       if (name.equals("shoot") && !keyPressed) {
        initVoyager();
        mult = 20;
        vPos = voyg_geo.getLocalTranslation();
        voyg_phy = new RigidBodyControl((float)voyg_m);
    
    
      
        voyg_phy.setGravity(Vector3f.ZERO);
       }
     }
    };

  public void initMaterials() {
    
    jupiter_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    jupiter_mat.setColor("Color", ColorRGBA.Brown);
    
    
    stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
    key2.setGenerateMips(true);
    Texture tex2 = assetManager.loadTexture(key2);
    stone_mat.setTexture("ColorMap", tex2);
  }

 public void initVoyager() {
   
    voyg_geo = new Geometry("voyager2", sphere);
    voyg_geo.setMaterial(stone_mat);
    rootNode.attachChild(voyg_geo);
    
    
    voyg_geo.setLocalTranslation(new Vector3f(0,32f,30f));
    
    voyg_phy = new RigidBodyControl((float)voyg_m);
    
    
    voyg_geo.addControl(voyg_phy);
    bulletAppState.getPhysicsSpace().add(voyg_phy);
    
    voyg_phy.setGravity(Vector3f.ZERO);
    
  }
   
  public void initJupiter(){
      
      jupiter_geo = new Geometry("Jupiter", jupiter);
      
      jupiter_geo.setMaterial(jupiter_mat);
      
      jupiter_phy = new RigidBodyControl((float)jupiter_m);
      
      jupiter_geo.addControl(jupiter_phy);
      bulletAppState.getPhysicsSpace().add(jupiter_phy);
      
      rootNode.attachChild(jupiter_geo);
      jupiter_phy.setGravity(Vector3f.ZERO);
      
      
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
          x0 -= 1f;
      }
      Geometry dot_geo1 = new Geometry("dot", dot);
      dot_geo1.setLocalTranslation(new Vector3f(0,0,x0+0.4f));
      dot_geo1.setMaterial(dot_mat);
      rootNode.attachChild(dot_geo1);
  }
  
  
  @Override 
  public void simpleUpdate(float tpf){
      
      vPos = voyg_geo.getLocalTranslation();
      distance(vPos);
      float x = checker ? vPos.z-0.2f : vPos.z+0.2f;
      
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
      
      double newGForce = force();
      double newVForce = vForce(y);
      
      Vector3f gForce = new Vector3f(0,(float)(-vPos.y*newGForce/l0),(float)(-vPos.z*newGForce/l0));
      vForce = new Vector3f(0, (float)((y-vPos.y)/newVForce-gForce.y), (float)((x -vPos.z)/newVForce-gForce.z));
      Vector3f absForce = new Vector3f(0, gForce.y + vForce.y,gForce.z + vForce.z);
      
      voyg_phy.setLinearVelocity(absForce.mult(mult/20));
  }
}