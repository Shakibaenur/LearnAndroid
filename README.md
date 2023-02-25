# LearnAndroid
As mobile technology continues to evolve, developers are increasingly tasked with creating camera applications that cater to users' diverse needs.<br>
One of the most popular tools for camera development is the CameraX library, which simplifies the process of creating custom camera applications in Android. 

In this article, we will walk you through the process of creating a custom camera using CameraX in Android.
Let's start…<br><br>

Add Dependency: <br><br>
Add CameraX Dependency First. We need to add the CameraX dependency to our project. 

To do this, we need to add the following code to our build.gradle file:
<p align="center">

<img src="https://github.com/Shakibaenur/CameraX/blob/master/images/sc1.png" width="550" title="build.gradle">

<p><br>

Set Up Permission:<br>

Set Up Camera Permissions Next. Need to set up camera permissions in our application's manifest file.<br>
We need to add the following code to our manifest file:
<p align="center">
  <img src="https://github.com/Shakibaenur/CameraX/blob/master/images/sc2.png" width="550" title="AndroidManifest">
  </p><br>
  Define in layout file: <br><br>
Define Preview View. Now define the Preview View for our camera.<br> We can do this by adding the following code to our xml layout file:
<p align="center">
  <img src="https://github.com/Shakibaenur/CameraX/blob/master/images/sc3.png" width="550" title="Layout">
  </p><br>
  
Check Permission in Activity file:<br><br>
In our activity class first we will have to check camera permission. Through allpermissionsGranted() function we are requesting for Camera permission and onRequestPermissionsResult() if all permissions granted ,
then we will start Camera by startCamera() function.
<p align="center">
  <img src="https://github.com/Shakibaenur/CameraX/blob/master/images/sc4.png" width="550" title="MainActivity">
  </p><br>
  That's it. Now we are good to work with CameraX preview.<br><br>
Let's Look at the preview.<br><br>

<p align="center">
  <img src="https://github.com/Shakibaenur/CameraX/blob/master/images/preview.gif" width="350" title="MainActivity">
  </p><br>
 

