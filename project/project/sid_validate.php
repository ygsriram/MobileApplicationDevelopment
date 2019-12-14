<?php
require_once 'include/DB_Functions.php';
include 'pws_func.php';
include 'mailer.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (!empty($_POST['sid']) ) {
	$deviceid=$_POST['deviceid'];
    // receiving the post params
    $sid = $_POST['sid'];
    if ($db->isUserExisted($sid)) {
        // Id is found
	$user=$db->login_status($sid);
	if(!$user)
	{
	$mailid=$db->getmailid($sid);
	$mail_id=$mailid["Email_ID"];
        $response["error"] = FALSE;
	$response["ide"]=1;
	$response["error_msg"] = "Login credentials are sent to your mail id $mail_id. Please check your spam folder. Report the problem if mail not recevied within 5 mins. Thank you"; 
	
	try {
	$password = random_str(6);
	} catch(Exception $e) {
	echo $e;
	}
	$response["pwd"] = $password;
	$db->storeUser($sid,$password,$deviceid);
	//sendmail($mailid["Name"],$mailid["Email_ID"],$password);
	echo json_encode($response);
	} else if($user["deviceid"]== $deviceid) {
		$response["error"] = FALSE;
		$response["deviceid"]=$deviceid;
		$user=$db->userdetails($sid);
		$response["Sid"] = $user["Sid"];
        	$response["user"]["Name"] = $user["Name"];
        	$response["user"]["Semester"] = $user["Semester"];
        	$response["user"]["Section"] = $user["Section"];
        	$response["user"]["Department"] = $user["Department"];
		$response["user"]["Email_ID"] = $user["Email_ID"];
        	$response["user"]["Mobile_number"] = $user["Mobile_number"];
		echo json_encode($response);
}
else {
	$response["error"] = 'TRUE';
	$response["error_msg"] = "You are already logged in. Please contact the admin.";
	echo json_encode($response);
	}
    	} else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "ID Entered is wrong, Please enter it properly";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter SID is missing!";
    echo json_encode($response);
}
?>
