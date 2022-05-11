Plade plade;
Knapper start;
Knapper exit;
Knapper spil_ovre;

enum Flag {
  STANDBY, NYTSPIL, EXITSPIL, POSITION, GAET
};

// enumeration type for variable error
enum Error {
  VARIABLEERROR, OK
};

// flag variables for control flag.
Flag flag;
Flag flag2;

// for error handling
Error error;

Tilstand vundet;

void setup() {
  size(600, 700);
  background(245);
  frameRate(30);

  flag = Flag.STANDBY;
  flag2 = Flag.POSITION;

  error = Error.OK;

  vundet = Tilstand.UDEFINERET;

  plade = new Plade(10);
  start = new Knapper(150.00, 150.00, "START");
  exit = new Knapper(150.00, 214.00, "Exit");
  spil_ovre = new Knapper(0.00, 500.00, "Done");
}

void draw() {
  background(255);

  // display little menu
  if (flag == Flag.STANDBY) {

    start.display();
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
    plade.display();
    spil_ovre.oplaast();
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
    text("SLUT", 150, 150);
  }

  switch (error)
  {
  case VARIABLEERROR:
    fill(0);
    textSize(20);
    text("Ikke nok skibe placeret", 260, 550);
    break;
  }
}

void mouseClicked()
{
  if(mouseButton == LEFT)
  {
    if(start.click())
    {
      start.displayEt(); // for knap animation.
      flag = Flag.NYTSPIL;
      start.laast();
      exit.laast();
    }

    if (exit.click())
    {
      exit.displayEt(); // for knap animation.
      flag = Flag.EXITSPIL;
      start.laast();
      exit.laast();
    }

    if (flag == Flag.NYTSPIL)
    {
      if (flag2 == Flag.POSITION)
      {
        plade.placerSkibe();
      } else if (flag2 == Flag.GAET)
      {
        plade.gaet();
        switch(plade.vundet())
        {
        case SPILLER:
          flag = Flag.STANDBY;
          flag2 = Flag.POSITION;
          start.oplaast();
          exit.oplaast();
          vundet = Tilstand.SPILLER;
          plade.genstart();
          break;
        case AI:
          flag = Flag.STANDBY;
          flag2 = Flag.POSITION;
          start.oplaast();
          exit.oplaast();
          vundet = Tilstand.AI;
          plade.genstart();
          break;
        }
      }

      if (spil_ovre.click())
      {
        spil_ovre.displayEt(); // for knap animation.
        if (plade.getSpillerSkibe() == plade.MAXSKIBE())
        {
          spil_ovre.laast();
          flag2 = Flag.GAET;
          error = Error.OK;
          plade.aiSkibe();
        }
        else
        {
          error = Error.VARIABLEERROR;
        }
      }
    }
  }
}
