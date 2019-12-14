<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array


if (isset($_POST['sid']) && isset($_POST['name']) && isset($_POST['dept']) && isset($_POST['email']) && isset($_POST['mobno']) && isset($_POST['sem']) && isset($_POST['sec'])) {
 
    // receiving the post params
	$sid = $_POST['sid'];
	$name = $_POST['name'];
	$dept = $_POST['dept'];
	$email = $_POST['email'];
	$mobno = $_POST['mobno'];
	$sem = $_POST['sem'];
	$sec = $_POST['sec'];
    
 
    // check if user is already existed with the same email
    if ($db->isUserExisted($sid)) {
        // user already existed
        echo "User already existed with " . $sid;
        
    } else {
        // create a new user
        $user = $db->storeUser($sid,$name,$dept,$email,$mobno,$sem,$sec);
        if ($user) {
            // user stored successfully
            echo "User with Student ID".$sid."Stored Succesfully";
        } else {
            // user failed to store
            echo "Unknown error occurred in registration!";
            
        }
    }
} else {
   echo "Required parameters are missing!";}
?>
