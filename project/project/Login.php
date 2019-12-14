<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 if (!empty($_POST['sid']) && !empty($_POST['password'])) {
 
    // receiving the post params
    $sid = $_POST['sid'];
    $password = $_POST['password'];
        // get the user by email and password
    $user = $db->validate_password($sid, $password);
 	if ($user != false) {
        // use is found
        $response["error"] = FALSE;
        $response["Sid"] = $user["Sid"];
        $response["user"]["Name"] = $user["Name"];
        $response["user"]["Semester"] = $user["Semester"];
        $response["user"]["Section"] = $user["Section"];
        $response["user"]["Department"] = $user["Department"];
	$response["user"]["Email_ID"] = $user["Email_ID"];
        $response["user"]["Mobile_number"] = $user["Mobile_number"];
	echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Password is wrong. Please enter correctly!!!!!";
if($count==3)
$response["error_msg"] = "Password is wrong. Contact admin!!!!!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Enter password!";
    echo json_encode($response);
}
?>
