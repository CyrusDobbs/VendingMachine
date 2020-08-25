-- BUILD & RUN --
Must have Java 8 installed.
You do not have to build the code because I have provided an up to date .jar file.
This can be ran by navigating to the directory of the jar in the terminal and entering:
    java -jar VendingMachine.jar

You can build the jar from the sourcecode (VendingMachine-master.zip) yourself using gradle if necessary.
I did this in intelliJ. The project is also on GitHub - https://github.com/CyrusDobbs/VendingMachine

Running the jar executes the interactive test-harness.

-- DECISIONS & JUSTIFICATIONS --
I have chosen to use int arrays (as opposed to a more complex object) to represent collections of coins coming in and
out of the system. This is because I understand the information needing to be passed in and out of this particular
system as a simple tally of coins.

I view the requirements to be asking for an application that:
    - keeps a count of a set of coins (started from an initialised amount)
    - allows a given set of coins to be deposited
    - gives change in the form of coins that sum to a given value

I understood this as to not include any requirement for the application to know of any 'transaction' taking place.
Instead, it must only be able to deal with the reasonably atomic and independent (to this system) functions of 'initialise',
'depositCoins' and 'returnChange'. I have chosen to add the functionality of returning the CoinTrackers internal state
as an int array as I expect this will be very useful to other components e.g responsible for deciding when to restock
coins in the machine / tell customers that the machine isn't giving change at the moment.

If the business was to expand overseas and start using a different set of coins in their machines then a new enum
containing the new coin types can be produced and used within the system (with a small amount of additional code such as to
choose the currency upon initialisation). Then, as long as the components connected to the CoinTracker were sending int
arrays of the same configuration then the system should work as it does with GB coins.

I have added a log system in order to track the inner workings of the component. (This is useful to view along
side the test-harness).

-- TEST HARNESS -- 
The test-harness simulates a human's selection of an item and of which coins to use to pay for the item. The coins are
then deposited and the change sum calculated and requested from the system. This makes use of all of the components
public methods. I have also written a set of JUnit tests.

The test-harness makes use of the items.csv & tracker_configs.csv. These can be changed in order to test the system
using specific values.
    items.csv - holds items for the user to buy (name,price)
    tracker_configs.csv - holds the initial coin configs of possible trackers (name,£2s,£1s,50ps,20ps,10ps,5ps,2ps,1ps)


