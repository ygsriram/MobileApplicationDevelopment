<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 $i=0;
// json response array
//$response = array("error" => FALSE);
 if (!empty($_POST['sid']) && !empty($_POST['password'])) {
 
    // receiving the post params
    $sid = $_POST['sid'];
    $password = $_POST['password'];
	$deviceid = $_POST['deviceid']; 
$count=$_POST["count"];
        // get the user by email and password
	$type1 = $db->type_user($sid);
    $user = $db->validate_password($sid, $password,$deviceid,$type1["type"]);
	if($user!=false){
	if (strcmp($type1["type"],"student")==0) {
        // use is found
	$abc=$db->sub_count($user["Semester"]);
	$j=$abc["count(*)"];
	$k=0;

	$sub = $db->sub_details($user["Semester"]);
        $response["error"] = FALSE;
        $response["Sid"] = $user["Sid"];
	$response["type"] = $type1["type"];
        $response["user"]["Name"] = $user["Name"];
        $response["user"]["Semester"] = $user["Semester"];
        $response["user"]["Section"] = $user["Section"];
        $response["user"]["Department"] = $user["Department"];
	$response["user"]["Email_ID"] = $user["Email_ID"];
        $response["user"]["Mobile_number"] = $user["Mobile_number"];
	while($i!=$j)
	{
	$response["subject"]["entry"]=$j;
	$response["subject"]["sub$i"] = $sub[$k];
	$response["subject"]["subcode$i"] = $sub[$k+1];
	$i++;
$k+=2;
	}
	echo json_encode($response);
}else {
	$abc=$db->fac_count($user["Fid"]);
	$j = $abc["count(*)"];
	$i=0;
	$k=0;
	$sub = $db->sub_handled($user["Fid"]);
	$response["error"]=FALSE;
	$response["fid"] = $user["Fid"];
	$response["type"] = $type1["type"];
	$response["user"]["Name"] = $user["Name"];
        $response["user"]["Department"] = $user["Department"];
	$response["user"]["Email_ID"] = $user["Email_ID"];
        $response["user"]["Mobile_number"] = $user["Mobile_Number"]; 
	while($i!=$j)
{
$response["subject"]["entry"]=$j;
	$response["subject"]["sub$i"] = $sub[$k];
	$response["subject"]["subcode$i"] = $sub[$k+1];
	$i++;
$k+=2;
}
echo json_encode($response);
}
}
else {
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
