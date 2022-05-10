import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Main_sketch_220310a extends PApplet {

int interval = 50;
int gentagelser = 0;
int sidsteTid = 0;

int[][] gitter;
int gitterStoerrelse = 40;

// slagskib længder
int hangarkib = 5;
int slagskib = 4;
int ubaad = 3;
int krydser = 3;
int destroyer = 2;

boolean spilOvre = false;
int skudAffyret = 0;
int skudRamt = 0;

String aktuelInstilling;
boolean maalIndstilling = false;
int targetModeAttempts = 4;
int sidsteMissilXakse = 0;
int sidsteMissilYakse = 0;

int skib = color(153, 165, 178);
int traeffer = color(228, 64, 93);
int misser = color(249, 118, 37);

final int SKIB = 1;
final int TRAEFFER = -1;
final int MISSER = -2;

public void setup() {
    
    // draw background of gitter
    stroke(50);
    
    // init. array
    gitter = new int[width/gitterStoerrelse][height/gitterStoerrelse];
    for(int x=0; x < width/gitterStoerrelse; x++) {
        for(int y=0; y< height/gitterStoerrelse; y++) {
            gitter[x][y] = 0;
        }
    }
    placerSkibe();
}

// Draw game state to canvas.
public void draw() {
    background(color(50, 57, 66));
    // draw gitter
    for (int x=0; x < width/gitterStoerrelse; x++) {
        for (int y=0; y < height/gitterStoerrelse; y++) {
           if(gitter[x][y] == SKIB) {
             fill(skib);
           } else if (gitter[x][y] == TRAEFFER) {
             fill(traeffer);
           } else if (gitter[x][y] == MISSER) {
             fill(misser);
           }
           rect(x*gitterStoerrelse, y*gitterStoerrelse, gitterStoerrelse, gitterStoerrelse);
           noFill();
       }
   }

   if (maalIndstilling) {
       aktuelInstilling = "maal";
   } else {
        aktuelInstilling = "skyd";
   }

   surface.setTitle("Krigsskib - Mode " + aktuelInstilling + " - Affyret: " +
                    parseInt(skudAffyret) + " Traeffer " + parseInt(skudRamt));

   if (spilOvre) {
       textSize(50);
       fill(255);
       text("Spil Færdigt!", width/4, height/2);
       noFill();
   }
   // gentag hvis timeren kører
   // millis() returner mængden af millisekunder siden programstart
   if(millis()-sidsteTid>interval) {
       // når timeren kører
       gentag();
       gentagelser++;
       sidsteTid = millis();
   }
}

// Determine if the game is over.
public boolean alleSkibeSmadret() {
     for(int x=0; x < width/gitterStoerrelse; x++) {
        for(int y=0; y< height/gitterStoerrelse; y++) {
            if (gitter[x][y] == SKIB) {
                return false;
            }
        }
    }
    spilOvre = true;
    return true;
}

// Add N E S W locations relative to fraXaksen and fraYaksen.
public ArrayList<PVector> findMaal(int fraXaksen, int fraYaksen) {
    ArrayList<PVector> koordinater = new ArrayList<PVector>();
    // FIX: this is naive  do not take into account whether the
    // coordinate has already been traeffer or missed.
    koordinater.add(new PVector(fraXaksen+1, fraYaksen));
    koordinater.add(new PVector(fraXaksen-1, fraYaksen));
    koordinater.add(new PVector(fraXaksen, fraYaksen+1));
    koordinater.add(new PVector(fraXaksen, fraYaksen-1));
    return koordinater;
}

// Iteration updates world state.
public void gentag() {
    if (alleSkibeSmadret()) {
        return;
    }

    int x = 0;
    int y = 0;

    if (maalIndstilling) {
        targetModeAttempts = targetModeAttempts - 1;
        ArrayList<PVector> tg = findMaal(sidsteMissilXakse, sidsteMissilYakse);
        PVector p = tg.get(targetModeAttempts);
        x = PApplet.parseInt(p.x);
        y = PApplet.parseInt(p.y);
        if (targetModeAttempts == 0) {
            maalIndstilling = false;
            targetModeAttempts = 4;
        }
    } else {
        x = PApplet.parseInt(random(width/gitterStoerrelse));
        y = PApplet.parseInt(random(height/gitterStoerrelse));
    }

    if (gitter[x][y] == SKIB) {
        //  traeffer et krigsskib
        gitter[x][y] = TRAEFFER;
        maalIndstilling = true;
        sidsteMissilXakse = x;
        sidsteMissilYakse = y;
        skudRamt++;
    } else if (gitter[x][y] != TRAEFFER && gitter[x][y] != SKIB) {
        //  missed
        gitter[x][y] = MISSER;
    }

    skudAffyret++;
}

// Placer skibe på gitter.
public void placerSkibe() {

    // hangarkib 5 lang
    gitter[1][1] = SKIB;
    gitter[1][2] = SKIB;
    gitter[1][3] = SKIB;
    gitter[1][4] = SKIB;
    gitter[1][5] = SKIB;

    // slagskib 4 lang
    gitter[5][6] = SKIB;
    gitter[6][6] = SKIB;
    gitter[7][6] = SKIB;
    gitter[8][6] = SKIB;

    // ubåd 3 lang
    gitter[8][2] = SKIB;
    gitter[8][3] = SKIB;
    gitter[8][4] = SKIB;

    // destroyer 2 lang
    gitter[14][7] = SKIB;
    gitter[15][7] = SKIB;

    // krydser 3 lang
    gitter[12][2] = SKIB;
    gitter[13][2] = SKIB;
    gitter[14][2] = SKIB;
}
public void setup() {  // setup() runs once
  size(800, 200);
  stroke(50);

}

public void draw() {  // draw() loops forever, until stopped
  background(204);
  yPos = yPos - 1.0f;
  if (yPos < 0) {
    yPos = height;
  }
  line(0, yPos, width, yPos);
}
  public void settings() {  size(800, 400, FX2D);  noSmooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Main_sketch_220310a" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
