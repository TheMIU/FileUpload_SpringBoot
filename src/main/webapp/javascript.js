$(document).ready(function () {
    $('#fileUploadForm').submit(function (e) {
        e.preventDefault();
        const formData = new FormData(this);
        $.ajax({
            url: '/file-upload/upload',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                alert("Upload success");
            },
            error: function (error) {
                alert("Upload failed " + error.responseText);
            }
        });
    });
});

/*
$(document).ready(function () {
    $('#fileUploadForm').submit(function (e) {
        e.preventDefault();
        const formData = new FormData(this);
        $.ajax({
            url: '/file-upload/upload',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                alert("Upload success " + response);
            },
            error: function (error) {
                alert("Upload failed " + error.responseText);
            }
        });
    });
});*/
