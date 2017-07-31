var pageSession = new ReactiveDict();

Template.ShowsEdit.rendered = function() {

};

Template.ShowsEdit.events({

});

Template.ShowsEdit.helpers({

});


Template.ShowsEditEditForm.rendered = function() {

    //this.ToEdit = new Mongo.Collection("toEdit");
    console.log(toEdit);
    document.getElementById("Title").value = toEdit.Title;
    document.getElementById("Rating").value = toEdit.Rating;
    document.getElementById("Type").value = toEdit.Type;
    console.log("Hi");
    console.log(updateId);
    console.log(updateObject);
    $('#form-other-button').click(function() {
        var values = toEdit;
        // Titles.remove({Title: toEdit.Title, Rating: toEdit.Rating, Type: toEdit.Type});
        updateId = values._id;
        removeObject = {Title: values.Title, Rating: values.Rating, Type: values.Type};
        values.Title = document.getElementById("Title").value;
        values.Rating = document.getElementById("Rating").value;
        values.Type = document.getElementById("Type").value;
        // Titles.insert(values);
        updateObject = values;
        console.log(Titles.findOne({Title: document.getElementById("Title").value, Rating: document.getElementById("Rating").value, Type: document.getElementById("Type").value}));
        Router.go("shows", {});
        return false;
    });
    $('#form-cancel-button').click(function() {
        Router.go("shows", {});
        return false;
    });
    console.log("Bye");


    pageSession.set("showsEditEditFormInfoMessage", "");
    pageSession.set("showsEditEditFormErrorMessage", "");

    $(".input-group.date").each(function() {
        var format = $(this).find("input[type='text']").attr("data-format");

        if(format) {
            format = format.toLowerCase();
        }
        else {
            format = "mm/dd/yyyy";
        }

        $(this).datepicker({
            autoclose: true,
            todayHighlight: true,
            todayBtn: true,
            forceParse: false,
            keyboardNavigation: false,
            format: format
        });
    });

    $("input[type='file']").fileinput();
    $("select[data-role='tagsinput']").tagsinput();
    $(".bootstrap-tagsinput").addClass("form-control");
    $("input[autofocus]").focus();
};

Template.ShowsInsertInsertForm.events({
    "submit": function(e, t) {
        console.log("Hello, world. We finally got it");
        e.preventDefault();
        pageSession.set("showsEditEditFormInfoMessage", "");
        pageSession.set("showsEditEditFormErrorMessage", "");

        var self = this;

        function submitAction(msg) {
            console.log("Hello world");
            var showsInsertInsertFormMode = "insert";
            if(!t.find("#form-cancel-button")) {
                switch(showsInsertInsertFormMode) {
                    case "insert": {
                        consol.log(":)")
                        $(e.target)[0].reset();
                    }; break;

                    case "update": {
                        console.log(":(");
                        var message = msg || "Saved.";
                        pageSession.set("showsEditEditFormInfoMessage", message);
                    }; break;
                }
            }

            Router.go("shows", {});
        }

        function errorAction(msg) {
            msg = msg || "";
            var message = msg.message || msg || "Error.";
            pageSession.set("showsEditEditFormErrorMessage", message);
        }

        validateForm(
            $(e.target),
            function(fieldName, fieldValue) {
                console.log("1");
            },
            function(msg) {
                console.log("2");
            },
            function(values) {
                console.log("3");
                newId = Titles.update(toEdit._id, values, function(e) { if(e) errorAction(e); else submitAction(); });
            }
        );

        return false;
    },
    "click #form-cancel-button": function(e, t) {
        e.preventDefault();



        Router.go("shows", {});
    },
    "click #form-close-button": function(e, t) {
        e.preventDefault();

        /*CLOSE_REDIRECT*/
    },
    "click #form-back-button": function(e, t) {
        e.preventDefault();

        /*BACK_REDIRECT*/
    }


});

Template.ShowsInsertInsertForm.helpers({
    "infoMessage": function() {
        return pageSession.get("showsEditEditFormInfoMessage");
    },
    "errorMessage": function() {
        return pageSession.get("showsEditEditFormErrorMessage");
    }

});
