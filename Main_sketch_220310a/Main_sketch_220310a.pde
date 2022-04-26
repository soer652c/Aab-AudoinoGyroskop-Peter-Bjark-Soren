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

color skib = color(153, 165, 178);
color traeffer = color(228, 64, 93);
color misser = color(249, 118, 37);

final int SKIB = 1;
final int TRAEFFER = -1;
final int MISSER = -2;

void setup() {
    size(641, 361, FX2D);
    // draw background of gitter
    stroke(50);
    noSmooth();
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
void draw() {
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
boolean alleSkibeSmadret() {
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
ArrayList<PVector> findMaal(int fraXaksen, int fraYaksen) {
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
void gentag() {
    if (alleSkibeSmadret()) {
        return;
    }

    int x = 0;
    int y = 0;

    if (maalIndstilling) {
        targetModeAttempts = targetModeAttempts - 1;
        ArrayList<PVector> tg = findMaal(sidsteMissilXakse, sidsteMissilYakse);
        PVector p = tg.get(targetModeAttempts);
        x = int(p.x);
        y = int(p.y);
        if (targetModeAttempts == 0) {
            maalIndstilling = false;
            targetModeAttempts = 4;
        }
    } else {
        x = int(random(width/gitterStoerrelse));
        y = int(random(height/gitterStoerrelse));
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
void placerSkibe() {

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
