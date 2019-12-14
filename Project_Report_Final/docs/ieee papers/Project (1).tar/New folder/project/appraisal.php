<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
$fid = $_POST["fid"];
$subcode = $_POST["subcode"];
$appr = $db->getappraisal($fid,$subcode);
$abc = $db->lasttaken($fid,$subcode);
$subject = $db->fid_subname($subcode);
$hcot = $db->getheadcount1($fid,$subcode);
if($appr["avg(Rating)"]!=null){
$response["error"] = FALSE;
$response["noofstars"]=$appr["avg(Rating)"];
$response["subcode"] = $subject["subject"];
$response["time"] = $abc["time"];
$response["hcot"] = $hcot["count(*)"];
echo json_encode($response);
}
else
{
$response["error"] = FALSE;
$response["noofstars"]=0.0;
$response["subcode"] = $subject["subject"];
$response["hcot"] = 0;
$response["time"] = date("Y-m-d");
echo json_encode($response);
}
?>
