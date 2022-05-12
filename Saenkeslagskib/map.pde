class Plade
{
  private Skib [][] kort;
  private int A;
  private int spillerSkibe;
  private int aiSkibe;
  private final int MAXIMUMSKIBEPLACERET = 5;

  public Plade(int A)
  {
    this.A = A;
    spillerSkibe = 0;

    kort = new Skib[A][A];

    Skib skib = null;

    for (int g = 0; g < A; g++)
    {
      for (int h = 0; h < A; h++)
      {

        skib = new Skib(50*g, 50*h);
        kort[g][h] = skib;
      }
    }
  }

  public void display()
  {
    for (int g = 0; g < A; g++)
    {
      for (int h = 0; h < A; h++)
      {
        kort[g][h].display();
      }
    }
  }


  public void placerSkibe()
  {

    for (int g = 0; g < A; g++)
    {
      for (int h = 0; h < A; h++)
      {
        if (kort[g][h].click())
        {
          if (kort[g][h].tilstandNu() == Tilstand.IKKESTARTET)
          {
            if (spillerSkibe < MAXIMUMSKIBEPLACERET)
            {
              kort[g][h].taendSpil(Tilstand.SPILLER);
              spillerSkibe++;
            }
          } else
          {
            kort[g][h].stopSpil(Tilstand.UDEFINERET);
            spillerSkibe--;
          }
        }
      }
    }
  }

  public void genererAiSkibe()
  {
    int skibCounter = 0;
    float randomValue = 0;
    for (int g = 0; g < A; g++)
    {
      for (int h = 0; h < A; h++)
      {

        if ((skibCounter < MAXIMUMSKIBEPLACERET) && (kort[g][h].getGH() != Tilstand.AI) && (kort[g][h].getGH() != Tilstand.SPILLER))
        {
          randomValue = random(1);
          if (randomValue >= 0.7 && randomValue <= 0.9 )
          {
            if (kort[g][h].getGH() != Tilstand.SPILLER)
            {
              kort[g][h].taendSpil(Tilstand.AI);
              skibCounter++;
            }
          }
        }
      }
    }

    if (skibCounter == 5)
    {
      aiSkibe = 5;
    } else
    {
      throw new RuntimeException("...to little ships!");
    }
  }

  public void guess()
  {
    Skib skib = null;
    int optaeller = 0;
    for (int g = 0; g < A; g++)
    {
      for (int h = 0; h < A; h++)
      {
        skib = kort[g][h];
        if (skib.click())
        {
          if (skib.getGH() == Tilstand.AI)
          {
            if (skib.tilstandNu() == Tilstand.STARTET)
            {

              skib.oedelagt();
              aiSkibe--;
            }
          } else
          {
            skib.markoer(Tilstand.SPILLER);
          }

          // pc guesses a ship.

          while (optaeller < 1)
          {
            skib = kort[(int)random(A)][(int)random(A)];
            while ((skib.getGH() == Tilstand.AI) || (skib.tilstandNu() == Tilstand.MARKOER)
              && (skib.tilstandNu() == Tilstand.RAMT))
            {
              skib = kort[(int)random(A)][(int)random(A)];
            }
            if (skib.getGH() == Tilstand.SPILLER)
            {
              if (skib.tilstandNu() == Tilstand.STARTET)
              {

                skib.oedelagt();
                optaeller++;
                spillerSkibe--;
              }
            } else
            {
              skib.markoer(Tilstand.AI);
              optaeller++;
            }
          }

          assert(optaeller == 1);
          optaeller = 0;
        }
      }
    }
  }

  public int getSpillerSkibe()
  {
    return spillerSkibe;
  }

  public int MAXSKIBE()
  {
    return MAXIMUMSKIBEPLACERET;
  }

  public Tilstand vundet()
  {
    if (aiSkibe == 0)
    {
      return Tilstand.SPILLER;
    }

    if (spillerSkibe == 0)
    {
      return Tilstand.AI;
    }

    return Tilstand.UDEFINERET;
  }


  public void genstart()
  {
    Skib skib = null;
    for (int g = 0; g < A; g++)
    {
      for (int h = 0; h < A; h++)
      {

        skib = new Skib(50*g, 50*h);
        kort[g][h] = skib;
      }
    }

    spillerSkibe = aiSkibe = 0;
  }
}
