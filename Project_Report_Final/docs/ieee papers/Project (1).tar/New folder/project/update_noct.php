<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
$subcode = $_POST["subcode"];
$sec = $_POST["sec"];
$db->inc_noct($subcode,$sec);
?>

