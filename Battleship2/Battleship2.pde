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

void setup() {
  size(600, 700);
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

void draw() {
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

void mouseClicked()
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
