<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// include database and object files
include_once './config/database.php';
include_once './objects/alarme.php';

// instantiate database and product object
$database = new Database();
$db = $database->getConnection();

// initialize object
$alarminfo = new Alarme($db);

// query alarm table
$stmt = $alarminfo->read();
$num = $stmt->rowCount();

// check if more than 0 record found
if($num>0){

    // alarm array
    $alarminfo_arr=array();

    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    $row = $stmt->fetch(PDO::FETCH_ASSOC);
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);

        $alarminfo_item=array(
            "estadoAtual" => $estadoAtual,
            "ativoDesde" => $tempoAtividade,
            "dataAtual" => $dataAtual,
            "intensidade" => $intensidade,
            "intervalo" => $intervalo,
            "tempoDeToque" => $tempoDeToque
        );

        array_push($alarminfo_arr, $alarminfo_item);

    echo json_encode($alarminfo_item);
}

else{
    echo json_encode(
        array("message" => "No alarm info found.")
    );
}
?>
