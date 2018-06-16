<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// include database and object files
include_once './config/database.php';
include_once './objects/tracking.php';

// instantiate database and product object
$database = new Database();
$db = $database->getConnection();

// initialize object
$tracking = new Tracking($db);

if (!isset($_GET['id'])) {
  die('{"message": "Missing parameter id.", "success": "0"}');
}

$tracking->id = $_GET['id'];

$stmt = $tracking->read();
$num = $stmt->rowCount();

// check if more than 0 record found
if($num>0){

    // products array
    $tracking_arr=array();

    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);

        $tracking_item=array(
            "entrada" => $entrada,
            "atual" => $atual,
            "pessoasNotificadas" => $pessoasNotificadas,
        );
        
        echo json_encode($tracking_item);
        return; 

        //array_push($tracking_arr, $tracking_item);
    }

    echo json_encode($tracking_arr);
}

else{
    http_response_code(404);
    echo json_encode(
        array("message" => "No tracking found.")
    );
}
?>
