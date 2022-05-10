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
        if (randomValue >= 0.7 && randomValue <= 0.9) {
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
