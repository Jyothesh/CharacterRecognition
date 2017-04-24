<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Character Recognition</title>

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js"></script>
    <script src="/CharacterRecognition/js/ocr.js"></script>
    <script type="text/javascript">
        jQuery(document).ready(init);
    </script>

    <link rel="stylesheet" href="/CharacterRecognition/css/bootstrap.min.css">
    <style type="text/css">
        html, body {
            background-color: #eee;
        }

        body {
            padding-top: 40px; /* 40px to make the container go all the way to the bottom of the topbar */
        }

        .container > footer p {
            text-align: center; /* center align it with the container */
        }

        .container {
            width: 820px; /* downsize our container to make the content feel a bit tighter and more cohesive. NOTE: this removes two full columns from the grid, meaning you only go to 14 columns and not 16. */
        }

            /* The white background content wrapper */
        .content {
            background-color: #fff;
            padding: 20px;
            margin: 0 -20px; /* negative indent the amount of the padding to maintain the grid system */
            -webkit-border-radius: 0 0 6px 6px;
            -moz-border-radius: 0 0 6px 6px;
            border-radius: 0 0 6px 6px;
            -webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .15);
            -moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .15);
            box-shadow: 0 1px 2px rgba(0, 0, 0, .15);
        }

            /* Page header tweaks */
        .page-header {
            background-color: #f5f5f5;
            padding: 20px 20px 10px;
            margin: -20px -20px 20px;
        }

            /* Styles you shouldn't keep as they are for displaying this base example only */
        .content .span10,
        .content .span4 {
            min-height: 500px;
        }

            /* Give a quick and non-cross-browser friendly divider */
        .content .span4 {
            margin-left: 0;
            padding-left: 19px;
            border-left: 1px solid #eee;
        }

        .topbar .btn {
            border: 0;
        }

    </style>

</head>

<body>
	<div class="container">
	    <div class="content">
	        <div class="page-header">
	            <h1>
	                <small>Digit Recognition</small>
	            </h1>
	        </div>
	        
	        <div class="row" style="padding-bottom: 20px">
	            <div class="span14" style="text-align: center">
	                <canvas id="digitView" width="80" height="80"
	                        style="border:1px solid #000000; background-color:#eeeeee; cursor: crosshair">
	                </canvas> <br/>
	
	                <img id="spinner" src="/CharacterRecognition/images/spinner.gif" style="display:none;"/>
	
	                <br/>
	
	                <div id="result" class="alert-message block-message success"
	                     style="width: 500px; margin-right:auto; margin-left:auto; display:none; text-align:justify;">
	                </div>
	
	                <div id="error" class="alert-message error"
	                     style="width: 400px; margin-right:auto; margin-left:auto; display:none">
	                </div>
	
	                <input id="recognize" type="button" value="Recognize Digit" class="btn primary"/>&nbsp;&nbsp;&nbsp;&nbsp;
	                <input id="clear" type="button" class="btn" value="Clear"/>
	            </div>
	        </div>
	    </div>
	</div>
</body>
</html>
