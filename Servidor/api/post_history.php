<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// get database connection
include_once './config/database.php';

// instantiate product object
include_once './objects/history.php';

$database = new Database();
$db = $database->getConnection();

$history = new History($db);

// get posted data
$data = json_decode(file_get_contents("php://input"));

// set history property values
$history->evento = $data->evento;

// create the product
if($history->create()){
    echo '{';
        echo '"message": "History saved successfully!"';
    echo '}';
}

// if unable to create the history, tell the user
else{
    echo '{';
        echo '"message": "Unable to save history."';
    echo '}';
}
?>
