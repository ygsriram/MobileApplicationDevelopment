<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
$classno = $_POST["classno"];
$loct = $db->getroomdetails($classno);
$response["error"]=FALSE;
$response["room"]["lat0"] = $loct["lat0"];
$response["room"]["lon0"] = $loct["lon0"];
$response["room"]["lat1"] = $loct["lat1"];
$response["room"]["lon1"] = $loct["lon1"];
$response["room"]["lat2"] = $loct["lat2"];
$response["room"]["lon2"] = $loct["lon2"];
$response["room"]["lat3"] = $loct["lat3"];
$response["room"]["lon3"] = $loct["lon3"];
echo json_encode($response);
?>

