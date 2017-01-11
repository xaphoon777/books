/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
Core JavaScript functionality for the application.  Performs the required
Restful calls, validates return values, and populates the book table.
 */
/* Builds the updated table for the book list */
function buildBookRows(books) {
    return _.template( $( "#book-tmpl" ).html(), {"books": books});
}

// Delete book
function deleteBook(id) {
    clearNotifications();
    if (id=="") {
        $('#formMsgs').append($('<span class="invalid">Book not selected. Select book from list before proceed</span>'));
        return;
    }

    // Display the loader widget
    $.mobile.loading("show");

    $.ajax({
        url: "rest/books/" + id,
        type: "DELETE",
        cache: false,
        success: function() {
            clearInputFields();
            $('#formMsgs').append($('<span class="success">Book deleted</span>'));
            $( "#book-table" ).table( "refresh" );
        },
        error: function(error) {
            if ((error.status == 409) || (error.status == 400)) {
                //console.log("Validation error registering user!");

                var errorMsg = $.parseJSON(error.responseText);

                $.each(errorMsg, function(index, val) {
                    $('<span class="invalid">' + val + '</span>').insertAfter($('#' + index));
                });
            } else if ((error.status == 404)) {
                //console.log("error - unknown server issue");
                $('#formMsgs').append($('<span class="invalid">Book not found</span>'));
            } else {
                //console.log("error - unknown server issue");
                $('#formMsgs').append($('<span class="invalid">Unknown server error</span>'));
            }
        },
        complete: function() {
            // Hide the loader widget
            $.mobile.loading("hide");
        }
    });
}

// Changes book's catalogue private to public and vice versa
function switchCatalogue (id) {
    clearNotifications();
    if (id=="") {
        $('#formMsgs').append($('<span class="invalid">Book not selected. Select book from list before proceed</span>'));
        return;
    }

    // Display the loader widget
    $.mobile.loading("show");

    $.ajax({
        url: "rest/books/" + id,
        type: "PUT",
        cache: false,
        success: function() {
            $('#formMsgs').append($('<span class="success">Catalogue changed</span>'));
            var catalog = $("#register-art  #catalogue").val();
            renderCatalogue(catalog, "viceVersa");
            $( "#book-table" ).table( "refresh" );
        },
        error: function(error) {
            if ((error.status == 409) || (error.status == 400)) {
                //console.log("Validation error registering user!");

                var errorMsg = $.parseJSON(error.responseText);

                $.each(errorMsg, function(index, val) {
                    $('<span class="invalid">' + val + '</span>').insertAfter($('#' + index));
                });
            } else if ((error.status == 404)) {
                //console.log("error - unknown server issue");
                $('#formMsgs').append($('<span class="invalid">Book not found</span>'));
            } else {
                //console.log("error - unknown server issue");
                $('#formMsgs').append($('<span class="invalid">Unknown server error</span>'));
            }
        },
        complete: function() {
            // Hide the loader widget
            $.mobile.loading("hide");
        }
    });
}

/* Uses JAX-RS GET to retrieve current book list */
function updateBookTable() {
    // Display the loader widget
    $.mobile.loading("show");

    $.ajax({
        url: "rest/books",
        cache: false,
        success: function(data) {
            $( "#books" ).empty().append(buildBookRows(data));
            $( "#book-table" ).table( "refresh" );
        },
        error: function(error) {
            //console.log("error updating table -" + error.status);
        },
        complete: function() {
            // Hide the loader widget
            $.mobile.loading("hide");
        }
    });
}

/*
Attempts to register a new book using a JAX-RS POST.  The callbacks
the refresh the book table, or process JAX-RS response codes to update
the validation errors.
 */
function registerBook(bookData) {
    //clear existing  msgs
    $('span.invalid').remove();
    $('span.success').remove();

    // Display the loader widget
    $.mobile.loading("show");

    $.ajax({
        url: 'rest/books',
        contentType: "application/json",
        dataType: "json",
        type: "POST",
        data: JSON.stringify(bookData),
        success: function(data) {
            
            clearNotifications();

            //mark success on the registration form
            $('#formMsgs').append($('<span class="success">Book saved</span>'));

            // make consistent form state for
            $("#register-art  #id").val(data.id);
            renderCatalogue("public");

            updateBookTable();
        },
        error: function(error) {
            if ((error.status == 409) || (error.status == 400)) {
                //console.log("Validation error registering user!");

                var errorMsg = $.parseJSON(error.responseText);

                $.each(errorMsg, function(index, val) {
                    $('<span class="invalid">' + val + '</span>').insertAfter($('#' + index));
                });
            } else {
                //console.log("error - unknown server issue");
                $('#formMsgs').append($('<span class="invalid">Unknown server error</span>'));
            }
        },
        complete: function() {
            // Hide the loader widget
            $.mobile.loading("hide");
        }
    });
}

function onRowClick(tr) {
    var id = $(tr).find("td>b:contains('Id')").parent().contents().last().text()
    var title = $(tr).find("td>b:contains('Title')").parent().contents().last().text();
    var author = $(tr).find("td>b:contains('Author')").parent().contents().last().text();
    var releaseDate = $(tr).find("td>b:contains('Release date')").parent().contents().last().text();
    var catalogue = $(tr).find("td>b:contains('Catalogue')").parent().contents().last().text();
    
    $("#register-art  #id").val(id);
    $("#register-art  #title").val(title);
    $("#register-art  #author").val(author);
    $("#register-art  #releaseDate").val(releaseDate);
    renderCatalogue(catalogue);
    $( ":mobile-pagecontainer" ).pagecontainer( "change", "#register-art", { role: "slide" } );
}

function clearInputFields() {
    //clear input fields
    $('#reg')[0].reset();
    // clear hidden id-field
    $('#id').val(null);
    renderCatalogue("public")
    //clear existing msgs
    $('span.invalid').remove();
    $('span.success').remove();
}

function clearNotifications() {
    $('span.invalid').remove();
    $('span.success').remove();
}

function renderCatalogue(catalog, viceVersa) {
    var condition = (viceVersa ? "private" : "public")
    if (catalog == condition) {
        $("#register-art  #catalogue").val("public");
        $("#register-art  #private").val(false);
    } else {
        $("#register-art  #catalogue").val("private");
        $("#register-art  #private").val(true);
    }
}