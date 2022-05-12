Plade plade;
Knapper start;
Knapper exit;
Knapper spil_ovre;


enum Marker {
  STANDBY, NYTSPIL, STOPSPIL, POSITION, GAET
};


enum Error {
  VARIABLEERROR, OK
};


Marker marker;
Marker marker2;


Error error;


Tilstand vundet;

void setup()
{
  size(500, 600);
  background(0, 0, 0);
  frameRate(60);

  marker = Marker.STANDBY;
  marker2 = Marker.POSITION;

  error = Error.OK;

  vundet = Tilstand.UDEFINERET;

  plade = new Plade(10);
  start = new Knapper(125, 150, "Placer Skibe");
  exit = new Knapper(125, 214, "Luk Spil");
  spil_ovre = new Knapper(0, 500, "Start");
}

void draw()
{
  background(105, 48, 10);


  if (marker == Marker.STANDBY)
  {

    start.display();
    exit.display();
    switch(vundet)
    {
    case SPILLER:
      fill(0);
      textSize(50);
      text("Du vandt :happyface:", 5, 140);
      break;
    case AI:
      fill(0);
      textSize(50);
      text("Du tabte :sadface:", 30, 140);
      break;
    }
  } else if (marker == Marker.NYTSPIL)
  {
    plade.display();
    spil_ovre.oplaast();
    vundet = Tilstand.UDEFINERET;
    if (marker2 == Marker.POSITION)
    {
      spil_ovre.display();
    }
  } else
  {
    fill(0);
    textSize(50);
    text("...", 150, 150);
  }

  switch (error)
  {
  case VARIABLEERROR:
    fill(0);
    textSize(20);
    text("Placer Flere Skibe!", 260, 550);
    break;
  }
}

void mouseClicked()
{
  if (mouseButton == LEFT)
  {
    if (start.click())
    {
      start.displayEt();
      marker = Marker.NYTSPIL;
      start.laast();
      exit.laast();
    }

    if (exit.click())
    {
      exit.displayEt();
      marker = Marker.STOPSPIL;
      start.laast();
      exit.laast();
    }

    if (marker == Marker.NYTSPIL)
    {
      if (marker2 == Marker.POSITION)
      {
        plade.placerSkibe();
      } else if (marker2 == Marker.GAET)
      {
        plade.guess();
        switch(plade.vundet())
        {
        case SPILLER:
          marker = Marker.STANDBY;
          marker2 = Marker.POSITION;
          start.oplaast();
          exit.oplaast();
          vundet = Tilstand.SPILLER;
          plade.genstart();
          break;
        case AI:
          marker = Marker.STANDBY;
          marker2 = Marker.POSITION;
          start.oplaast();
          exit.oplaast();
          vundet = Tilstand.AI;
          plade.genstart();
          break;
        }
      }

      if (spil_ovre.click())
      {
        spil_ovre.displayEt();
        if (plade.getSpillerSkibe() == plade.MAXSKIBE())
        {
          spil_ovre.laast();
          marker2 = Marker.GAET;
          error = Error.OK;
          plade.genererAiSkibe();
        } else
        {
          error = Error.VARIABLEERROR;
        }
      }
    }
  }
}
