<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: access");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Credentials: true");
header('Content-Type: application/json');


// include database and object files
include_once './config/database.php';
include_once './objects/historyTracking.php';

// instantiate database and product object
$database = new Database();
$db = $database->getConnection();

// initialize object
$history = new HistoryTracking($db);

// set ID property of product to be edited
$history->trackingId = isset($_GET['trackingId']) ? $_GET['trackingId'] : die();

// query products
$stmt = $history->read();
$num = $stmt->rowCount();

// check if more than 0 record found
if($num>0){

    // products array
    $history_arr=array();

    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);

        $history_item=array(
            "area" => $area,
            "duracao" => $duracao,
            "trackingId" => $trackingId,
        );

        array_push($history_arr, $history_item);
    }

    echo json_encode($history_arr);
}

else{
    echo json_encode(
        array("message" => "No history tracking found.")
    );
}
?>
