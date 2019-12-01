<form action="#" method="post">
    <label>
        Email:
        <input type="text" name="email">
    </label>
    <input type="submit" name="verification" value="Verification" />
</form>



<?php

if($_SERVER['REQUEST_METHOD'] == "POST" and isset($_POST['verification'])) {
    $email = $_REQUEST['email'];

    $ats = count_char($email, "@");
    if ($ats != 1) {
        alert("You have ". $ats. " at(s), but must be 1");
        return;
    }

    $dots = count_char($email, ".");
    if ($dots < 1) {
        alert("You have ". $dots. " dot(s), but must be more than 1");
        return;
    }

    $spaces = count_char($email, " ");
    if ($spaces > 0) {
        alert("You have ". $spaces. " space(s) is your email");
        return;
    }

    // Task 3
    $domain = [];
    preg_match("/@.*$/", $email, $domain);
    $domain = substr($domain[0], 1);

    $dots = count_char($domain, ".");
    if ($dots != 1) {
        alert("You have ". $dots. " dot(s) after \"@\", but must be 1");
        return;
    }

    echo "Domain: ". $domain;
}

function count_char($string, $char) {
    $letters = count_chars($string, 1);
    $count = $letters[ord($char)];
    return $count ? $count : 0;
}

function alert($message) {
    echo "<script type='text/javascript'>alert('$message');</script>";
}
