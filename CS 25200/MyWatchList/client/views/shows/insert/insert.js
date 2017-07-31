var pageSession = new ReactiveDict();

Template.ShowsInsert.rendered = function() {
	
};

Template.ShowsInsert.events({
	
});

Template.ShowsInsert.helpers({
	
});

Template.ShowsInsertInsertForm.rendered = function() {
	

	pageSession.set("showsInsertInsertFormInfoMessage", "");
	pageSession.set("showsInsertInsertFormErrorMessage", "");

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
		e.preventDefault();
		pageSession.set("showsInsertInsertFormInfoMessage", "");
		pageSession.set("showsInsertInsertFormErrorMessage", "");

		var self = this;

		function submitAction(msg) {
			var showsInsertInsertFormMode = "insert";
			if(!t.find("#form-cancel-button")) {
				switch(showsInsertInsertFormMode) {
					case "insert": {
						$(e.target)[0].reset();
					}; break;

					case "update": {
						var message = msg || "Saved.";
						pageSession.set("showsInsertInsertFormInfoMessage", message);
					}; break;
				}
			}

			Router.go("shows", {});
		}

		function errorAction(msg) {
			msg = msg || "";
			var message = msg.message || msg || "Error.";
			pageSession.set("showsInsertInsertFormErrorMessage", message);
		}

		validateForm(
			$(e.target),
			function(fieldName, fieldValue) {

			},
			function(msg) {

			},
			function(values) {
				console.log(values)

				newId = Titles.insert(values, function(e) { if(e) errorAction(e); else submitAction(); });
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
		return pageSession.get("showsInsertInsertFormInfoMessage");
	},
	"errorMessage": function() {
		return pageSession.get("showsInsertInsertFormErrorMessage");
	}
	
});
