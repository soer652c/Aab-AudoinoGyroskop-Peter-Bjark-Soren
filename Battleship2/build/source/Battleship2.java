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

public class Battleship2 extends PApplet {

Map map;
Knapper start;
Knapper exit;
Knapper spil_ovre;

enum Flag {
  STANDBY, NYTSPIL, EXITSPIL, POSITION, GAET
};

// enumeration type for variable error
enum Error {
  VARIABLEERROR, OK
}

// flag variables for control flag.
Flag flag;
Flag flag2;

// for error handling
Error error;

Tilstand vundet;

public void setup() {
  
  background(245);
  frameRate(30);

  flag = Flag.STANDBY;
  flag2 = Flag.POSITION;

  error = Error.OK;

  vundet = Tilstand.UDEFINERET;

  map = new Map(10);
  start = new Knapper(150, 150, "START");
  exit = new Knapper(150, 214, "Exit");
  spil_ovre = new Knapper(0, 500, "Done");
}

public void draw() {
  background(255);

  // display little menu
  if (flag == Flag.STANDBY) {

    start_spil.display();
    exit.display();
    switch(vundet) {
    case SPILLER:
      fill(0);
      textSize(50);
      text("You vundet!", 150, 150);
      break;
    case AI:
      fill(0);
      textSize(50);
      text("You lose!", 150, 150);
      break;
    }
  }
  else if (flag == Flag.NYTSPIL) {
    map.display();
    spil_ovre.unlock();
    vundet = Tilstand.UDEFINERET;
    if (flag2 == Flag.POSITION)
    {
      spil_ovre.display();
    }
  }
  else
  {
    fill(0);
    textSize(50);
    text("ENDE", 150, 150);
  }

  switch (error)
  {
  case VARIABLEERROR:
    fill(0);
    textSize(20);
    text("Zu wenig Schiffe", 260, 550);
    break;
  }
}

public void mouseClicked()
{
  if (mouseButton == LEFT)
  {
    if (start_spil.click())
    {
      start_spil.display1(); // for knap animation.
      flag = Flag.NYTSPIL;
      start_spil.lock();
      exit.lock();
    }

    if (exit.click())
    {
      exit.display1(); // for knap animation.
      flag = Flag.EXITSPIL;
      start_spil.lock();
      exit.lock();
    }

    if (flag == Flag.NYTSPIL)
    {
      if (flag2 == Flag.POSITION)
      {
        map.setShip();
      } else if (flag2 == Flag.GAET)
      {
        map.guess();
        switch(map.vundet())
        {
        case SPILLER:
          flag = Flag.STANDBY;
          flag2 = Flag.POSITION;
          start_spil.unlock();
          exit.unlock();
          vundet = Tilstand.SPILLER;
          map.reset();
          break;
        case AI:
          flag = Flag.STANDBY;
          flag2 = Flag.POSITION;
          start_spil.unlock();
          exit.unlock();
          vundet = Tilstand.AI;
          map.reset();
          break;
        }
      }

      if (spil_ovre.click())
      {
        spil_ovre.display1(); // for knap animation.
        if (map.getNumShips() == map.MAX())
        {
          spil_ovre.lock();
          flag2 = Flag.GAET;
          error = Error.OK;
          map.createShipsForPC();
        }
        else
        {
          error = Error.VARIABLEERROR;
        }
      }
    }
  }
}
class Knapper {
  private float yAksen;
  private float xAksen;
  private float brede;
  private float hoejde;
  private boolean tilstand;
  private int indeks;
  private String navn;
  private PImage [] piimgs;
  private PImage knapbilleder;

  public Knap(float yAksen, float xAksen, Sting navn) {
    this.yAksen = yAksen;
    this.xAksen = xAksen;
    this.navn = navn;
    brede = 300;
    hoejde = 75;
    tilstand = true;

    indeks = 0;
    knapbilleder = loadImage("knap.png");
    piimgs = new PImage[2];
    piimgs[0] = knapbilleder.get(0, 64, 256, 64);
    piimgs[1] = knapbilleder.get(0, 0, 256, 64);
  }

  public void display() {
    final float FORSKYDNING = 32;
    image(piimgs[0], xAksen, yAksen);
    fill(0);
    textSize(30);
    text(navn, yAksen+FORSKYDNING, xAksen+FORSKYDNING);
  }

  public void displayEt() {
    final float FORSKYDNING = 32;
    image(piimgs[1], xAksen, yAksen);
    fill(0);
    textSize(30);
    text(navn, yAksen+FORSKYDNING, xAksen+FORSKYDNING);
  }

  public boolean mouseClicked() {
    if(tilstand && (mouseY >= yAksen) && (mouseY <= yAksen+hoejde) && (mouseX >= xAksen) && (mouseX <= xAksen+brede)) {
      indeks = 1;
      return true;
    }
    else {
      indeks = 0;
      return false;
    }
  }

  public void laast() {
    tilstand = false;
  }

  public void oplaast() {
    tilstand = true;
  }
}
class Map {
  private int A; //Navn på int
  private int spillerSkibe; //Spillerens Skibe
  private int aiSkibe; //Computerens Skibe
  private final int MAKSIMUMSKIBEPLACERET = 5; //Den maksimale mængde skibe spilleren kan placere
  private Skib [][] map;

  public Map(int A)
  {
    this.A = A;
    spillerSkibe = 0;
    map = new Skib[A][A];
    Skib skib = null;

    for(int g = 0; g < A; g++)
    {
      for(int h = 0; h < A; h++)
      {
        skib = new Skib(50*g, 50*h);
        map[g][h] = skib;
      }
    }
  }

public void display()
{
  for (int g = 0; g < A; g++)
  {
    for (int h = 0; h < A; h++)
    {
      map[g][h].display();
    }
  }
}
public void placerSkibe()
{
  for (int g = 0; g < A; g++)
  {
    for (int h = 0; h < A; h++)
    {
      if (map[g][h].mouseClicked())
      {
        if (map[g][h].ilstandNu() == Tilstand.IKKESTARTET)
        {
          if (spillerSkibe < MAKSIMUMSKIBEPLACERET) {
            map[g][h].tilstandTaendt(Tilstand.SPILLER);
            spillerSkibe++;
        }
      }
      else {
        map[g][h].stopSpil(Tilstand.STOPPET);
        spillerSkibe--;
      }
    }
    }
  }
}

public void aiSkibe() {
  int skibCounter = 0;
  float randomValue = 0;
  for (int g = 0; g < A; g++) {
    for (int h = 0; h < A; h++) {
      if ((skibCounter < MAKSIMUMSKIBEPLACERET) && (map[g][h].getGH() != Tilstand.AI) && (map[g][h].getGH() != Tilstand.SPILLER)) {
        randomValue = random(1);
        if (randomValue >= 0.7f && randomValue <= 0.9f) {
          if (map[g][h].getGH() != Tilstand.SPILLER) {
            map[g][h].tilstandTaendt(Tilstand.AI);
            skibCounter++;
          }
        }
      }
    }
  }
if (skibCounter == 5) {
  aiSkibe = 5;
}
else {
  throw new RuntimeException("placer flere skibe");
}
}

public void gaet() {
  Skib skib = null;
  int optaeller = 0;
  for (int g = 0; g < A; g++) {
    for (int h = 0; h < A; h++) {
      skib = map[g][h];
      if (skib.mouseClicked()) {
        if (skib.getGH() == Tilstand.AI) {
          if (skib.tilstandNu == Tilstand.STARTET) {
            skib.ramt();
            aiSkibe--;
            }
          }
          else {
            skib.marker(Tilstand.SPILLER);
          }
          while (optaeller < 1) {
            skib = map[(int)random(A)][(int)random(A)];
            while ((skib.getGH() == Tilstand.AI) || (skib.tilstandNu() == Tilstand.MARKOER) && (skib.tilstandNu() == Tilstand.RAMT)) {
              skib = map[(int)random(A)][(int)random(A)];
            }
            if (skib.getGH() == Tilstand.SPILLER) {
              if (skib.tilstandNu() == Tilstand.STARTET) {
                skib.ramt();
                optaeller++;
                spillerSkibe--;
              }
              else {
                skib.ramt(Tilstand.AI);
                optaeller++;
              }
            }
          }
          assert(optaeller == 1);
          optaeller = 0;
        }
      }
    }
  }
public int getSpillerSkibe() {
  return spillerSkibe;
}
public int MAXIMUMSKIBE() {
  MAXIMUMSKIBE;
}
public Tilstand vundet() {
  if (aiSkibe == 0){
    return Tilstand.SPILLER;
  }
  if (spillerSkibe == 0) {
    return Tilstand.AI;
  }
  return Tilstand.STOPPET;
}

public void genstart() {
  Skib skib = null;
  for (int g = 0; g < A; g++) {
    for (int h = 0; h < A; h++) {
      skib = new Skib(50*g, 50*h);
      map[g][h] = skib;
    }
  }
  spillerSkibe = aiSkibe = 0;
}
}
enum Tilstand {
  STARTET, IKKESTARTET, RAMT, SPILLER, AI, UDEFINERET, MARKOER
};

class Skib {
  private Tilstand gh;
  private Tilstand tilstand;
  private float kant;
  private float yAksen;
  private float xAksen;
  private PImage piimg;

  public Skib() {
    this.yAksen = 0;
    this.xAksen = 0;
    this.tilstand = Tilstand.IKKESTARTET;
    gh = Tilstand.UDEFINERET;
    kant = 50;
    piimg = loadImage("krigsskib.png");
  }

  public Skib(float yAksen, float xAksen) {
    this.yAksen = yAksen;
    this.xAksen = xAksen;
    this.tilstand = Tilstand.IKKESTARTET;
    gh = Tilstand.UDEFINERET;
    kant = 50;
    piimg = loadImage("krigsskib.png");
  }

  public Tilstand tilstandNu() {
    return tilstand;
  }

  public Tilstand getGH() {
    return gh;
  }

  public void stopSpil(Tilstand h) {
    tilstand = Tilstand.IKKESTARTET;
    gh = h;
  }

  public void ramt() {
    tilstand = Tilstand.RAMT;
  }

  public void display() {
    final float KANT = 50;
    final float HALVKANT = KANT * 0.5f;
    if(tilstand == Tilstand.STARTET) {
      fill(245, 0, 0);
      stroke(195, 224, 58);
      rect(yAksen, xAksen, KANT, KANT);
      if(gh == tilstand.SPILLER) {
        image(piimg, xAksen, yAksen);
      }
      else if(tilstand == Tilstand.IKKESTARTET) {
        fill(245, 0, 0);
        stroke(195, 224, 58);
        rect(yAksen, xAksen, KANT, KANT);
      }
      else if(tilstand == Tilstand.RAMT); {
        fill(245, 0, 0);
        stroke(195, 224, 58);
        rect(yAksen, xAksen, KANT, KANT);
        if(gh == Tilstand.SPILLER) {
            stroke(195, 224, 58);
          }
          else if(gh == Tilstand.AI) {
            stroke(195, 224, 58);
          }
          line(xAksen, yAksen, xAksen+KANT, yAksen+KANT);
          line(xAksen, yAksen+KANT, xAksen+KANT, yAksen);
        }
      }
      else if(tilstand == Tilstand.MARKOER) {
        if(gh == Tilstand.SPILLER) {
          fill(245, 0, 0);
        }
        else {
          fill(0, 245, 0);
        }
        stroke(195, 224, 58);
        rect(yAksen, xAksen, KANT, KANT);
      }
    }

  }

  public boolean mouseClicked() {
    if((mouseY >= yAksen) && (mouseY <= yAksen + kant) && (mouseX >= xAksen) && (mouseX <= xAksen + kant)) {
      return true;
    }
    else {
      return false;
    }
  }

  public void markoer(Tilstand h) {
    if(gh == Tilstand.UDEFINERET) {
      tilstand = Tilstand.MARKOER;
      gh = h;
    }
  }
  public void settings() {  size(600, 700); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Battleship2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
