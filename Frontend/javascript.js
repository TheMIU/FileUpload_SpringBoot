const fileInput = document.getElementById('fileInput');
const previewImage = document.getElementById('preview');
let baseURL = "http://localhost:8080/file-upload/"
let filename;

loadAndDisplayImages();

resetForm();

function resetForm() {
    fileInput.value = '';
    previewImage.style.display = 'none';
    $('#progressBar').width(0);
    $('#progress-div').hide();
}

// event listener to check if file selected using event listener
fileInput.addEventListener('change', function () {
    const file = fileInput.files[0];
    if (file) {
        console.log(file);
        filename = file.name
        console.log('Selected file: ' + filename);

        // Read the selected file and display it in the preview, show progress bar
        const reader = new FileReader();
        reader.onload = function (e) {
            previewImage.style.display = 'block';
            previewImage.src = e.target.result;
            $('#progress-div').show();
        };
        reader.readAsDataURL(file);
    }

    if (!file) {
        resetForm();
    }
});

// upload file
$(document).ready(function () {
    $('#fileUploadForm').submit(function (e) {
        e.preventDefault();
        const formData = new FormData(this);

        if (fileInput.files.length === 0) {
            alert('Please select a file before uploading.');
            return; // Don't proceed with the upload if no file is selected
        }

        $.ajax({
            url: baseURL + 'upload',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,

            xhr: function () {
                // manage and monitor the progress of a file upload when making an asynchronous HTTP request to a server,
                // enabling real-time updates of the progress, such as for updating a progress bar
                const xhr = new window.XMLHttpRequest();
                xhr.upload.addEventListener("progress", function (evt) {
                    console.log(evt)
                    if (evt.lengthComputable) {
                        const percentComplete = (evt.loaded / evt.total) * 100;
                        $('#progressBar').width(percentComplete + '%'); // Update the progress bar
                    }
                }, false);
                return xhr;
            },
            success: function () {
                loadAndDisplayImages();
                alert("Done !");
                resetForm();
            },
            error: function (error) {
                alert("Upload failed " + error.responseText);
                resetForm();
            }
        });
    });
});

// Function to load and display images
function loadAndDisplayImages() {
    $.ajax({
        url: baseURL + 'list-images',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            const imageTable = $('#image-table');
            imageTable.empty();

            console.log(data)
            data.forEach(function (imageName, index) {
                const newRow = `
                        <tr>
                            <th scope="row">${index + 1}</th>
                            <td>${imageName}</td>
                            <!--get image one by one from server-->
                           <td><img src= "${baseURL+imageName}" alt="Image" style="max-width: 100px;"></td>
                        </tr>
                    `;
                imageTable.append(newRow);
            });
        },
        error: function (error) {
            alert("Failed to load images: " + error.responseText);
        }
    });
}