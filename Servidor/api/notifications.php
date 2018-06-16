<?php
define('API_ACCESS_KEY', 'AAAA6ekUT6A:APA91bHUHB-AsP_N5Tz4MN7jMtbYQQgVTEZN-4f-3TzXe0S7BLcMCso578SqXchgzdKXA7xqIXDJ_WhnPCv1H2dTuJGWBAC_c31uRaF5Vmotr4j0jS71gGQBm-gTiji-lv0vYxfoMgPk'); // API access key from Firebase Console
$registrationIds = array(''); //Token IDs of devices

$msg = array
(
    'text'  => 'A burglar broke into you house!',
    'title'     => 'Warning',
);

$fields = array
(
    'to'    => $registrationIds[0],
    'notification'      => $msg,
    "priority"=> "high"
);

$headers = array
(
    'Authorization: key=' . API_ACCESS_KEY,
    'Content-Type: application/json',
);

$ch = curl_init();
curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
curl_setopt( $ch,CURLOPT_POST, true );
curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ));
$result = curl_exec($ch );
curl_close( $ch );
echo $result;
?>
