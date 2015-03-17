# BigThree #

BigThree is a simple Android application that keeps track of weight-lifting maximum lifts. It targets Android 2.3.x. There are changes that would have to be made to run this application properly on subsequent versions. However, this was just an exercise in getting familiar with the development cycle for an Android application.

## Details ##
The idea behind this calculator is pretty simple. Some weight lifters, powerlifters in particular, are interested in progress they make on the "big three" lifts: deadlift, squat, and bench press. However, attempts to lift your one-rep max (1RM) are not made often. Because of this, there are several popular algorithms that calculate a theoretical 1RM based on the weight and number of reps performed.

BigThree allows the user to specify any number of exercises and track them individually. For any given workout, the user records their top set and the app calculates the theoretical 1RM. Records can then be viewed in table or graph form. BigThree uses AChartEngine (https://code.google.com/p/achartengine/) for displaying graphs. This allows easy visualization of progress on all tracked exercises.

Additional features include:
  * Theoretical max calculator
  * Percent of 1RM calculator
  * Export all records to a CSV file