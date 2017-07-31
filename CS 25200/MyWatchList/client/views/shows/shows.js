var pageSession = new ReactiveDict();

Template.Shows.rendered = function() {
	
};

Template.Shows.events({
	
});

Template.Shows.helpers({
	
});

var ShowsViewItems = function(cursor) {
	if(!cursor) {
		return [];
	}

	var searchString = pageSession.get("ShowsViewSearchString");
	var sortBy = pageSession.get("ShowsViewSortBy");
	var sortAscending = pageSession.get("ShowsViewSortAscending");
	if(typeof(sortAscending) == "undefined") sortAscending = true;

	var raw = cursor.fetch();

	// filter
	var filtered = [];
	if(!searchString || searchString == "") {
		filtered = raw;
	} else {
		searchString = searchString.replace(".", "\\.");
		var regEx = new RegExp(searchString, "i");
		var searchFields = ["Title", "Rating", "Type"];
		filtered = _.filter(raw, function(item) {
			var match = false;
			_.each(searchFields, function(field) {
				var value = (getPropertyValue(field, item) || "") + "";

				match = match || (value && value.match(regEx));
				if(match) {
					return false;
				}
			})
			return match;
		});
	}

	// sort
	if(sortBy) {
		filtered = _.sortBy(filtered, sortBy);

		// descending?
		if(!sortAscending) {
			filtered = filtered.reverse();
		}
	}

	return filtered;
};

var ShowsViewExport = function(cursor, fileType) {
	var data = ShowsViewItems(cursor);
	var exportFields = ["Title", "Rating", "Type"];

	var str = convertArrayOfObjects(data, exportFields, fileType);

	var filename = "export." + fileType;

	downloadLocalResource(str, filename, "application/octet-stream");
};

Template.ShowsView.rendered = function() {
	pageSession.set("ShowsViewStyle", "table");
	console.log("Here we go...");
	console.log(updateId);
	console.log(updateObject);
	if (updateObject != null) {
		console.log("Doing it...");
		Titles.remove(updateId);
		Titles.insert(updateObject);
		console.log("Did it....");
	}
};

Template.ShowsView.events({
	"submit #dataview-controls": function(e, t) {
		return false;
	},

	"click #dataview-search-button": function(e, t) {
		e.preventDefault();
		var form = $(e.currentTarget).parent();
		if(form) {
			var searchInput = form.find("#dataview-search-input");
			if(searchInput) {
				searchInput.focus();
				var searchString = searchInput.val();
				pageSession.set("ShowsViewSearchString", searchString);
			}

		}
		return false;
	},

	"keydown #dataview-search-input": function(e, t) {
		if(e.which === 13)
		{
			e.preventDefault();
			var form = $(e.currentTarget).parent();
			if(form) {
				var searchInput = form.find("#dataview-search-input");
				if(searchInput) {
					var searchString = searchInput.val();
					pageSession.set("ShowsViewSearchString", searchString);
				}

			}
			return false;
		}

		if(e.which === 27)
		{
			e.preventDefault();
			var form = $(e.currentTarget).parent();
			if(form) {
				var searchInput = form.find("#dataview-search-input");
				if(searchInput) {
					searchInput.val("");
					pageSession.set("ShowsViewSearchString", "");
				}

			}
			return false;
		}

		return true;
	},

	"click #dataview-insert-button": function(e, t) {
		e.preventDefault();
		Router.go("shows.insert", {});
	},

	"click #dataview-export-default": function(e, t) {
		e.preventDefault();
		ShowsViewExport(this.titles, "csv");
	},

	"click #dataview-export-csv": function(e, t) {
		e.preventDefault();
		ShowsViewExport(this.titles, "csv");
	},

	"click #dataview-export-tsv": function(e, t) {
		e.preventDefault();
		ShowsViewExport(this.titles, "tsv");
	},

	"click #dataview-export-json": function(e, t) {
		e.preventDefault();
		ShowsViewExport(this.titles, "json");
	}

	
});

Template.ShowsView.helpers({

	"insertButtonClass": function() {
		return Titles.userCanInsert(Meteor.userId(), {}) ? "" : "hidden";
	},

	"isEmpty": function() {
		return !this.titles || this.titles.count() == 0;
	},
	"isNotEmpty": function() {
		return this.titles && this.titles.count() > 0;
	},
	"isNotFound": function() {
		return this.titles && pageSession.get("ShowsViewSearchString") && ShowsViewItems(this.titles).length == 0;
	},
	"searchString": function() {
		return pageSession.get("ShowsViewSearchString");
	},
	"viewAsTable": function() {
		return pageSession.get("ShowsViewStyle") == "table";
	},
	"viewAsList": function() {
		return pageSession.get("ShowsViewStyle") == "list";
	},
	"viewAsGallery": function() {
		return pageSession.get("ShowsViewStyle") == "gallery";
	}

	
});


Template.ShowsViewTable.rendered = function() {
	
};

Template.ShowsViewTable.events({
	"click .th-sortable": function(e, t) {
		e.preventDefault();
		var oldSortBy = pageSession.get("ShowsViewSortBy");
		var newSortBy = $(e.target).attr("data-sort");

		pageSession.set("ShowsViewSortBy", newSortBy);
		if(oldSortBy == newSortBy) {
			var sortAscending = pageSession.get("ShowsViewSortAscending") || false;
			pageSession.set("ShowsViewSortAscending", !sortAscending);
		} else {
			pageSession.set("ShowsViewSortAscending", true);
		}
	}
});

Template.ShowsViewTable.helpers({
	"tableItems": function() {
		return ShowsViewItems(this.titles);
	}
});


Template.ShowsViewTableItems.rendered = function() {
	
};

Template.ShowsViewTableItems.events({
	"click td": function(e, t) {
		e.preventDefault();
		
		Router.go("titles.details", {titleId: this._id});
		return false;
	},

	"click .inline-checkbox": function(e, t) {
		e.preventDefault();

		if(!this || !this._id) return false;

		var fieldName = $(e.currentTarget).attr("data-field");
		if(!fieldName) return false;

		var values = {};
		values[fieldName] = !this[fieldName];

		Titles.update({ _id: this._id }, { $set: values });

		return false;
	},

	"click #delete-button": function(e, t) {
		e.preventDefault();
		var me = this;
		bootbox.dialog({
			message: "Delete? Are you sure?",
			title: "Delete",
			animate: false,
			buttons: {
				success: {
					label: "Yes",
					className: "btn-success",
					callback: function() {
						Titles.remove({ _id: me._id });
					}
				},
				danger: {
					label: "No",
					className: "btn-default"
				}
			}
		});
		return false;
	},
	"click #edit-button": function(e, t) {
		e.preventDefault();
		toEdit = this;
		Router.go("shows.edit", {titleId: this._id});
		return false;
	}
});

Template.ShowsViewTableItems.helpers({
	"checked": function(value) { return value ? "checked" : "" }, 
	"editButtonClass": function() {
		return Titles.userCanUpdate(Meteor.userId(), this) ? "" : "hidden";
	},

	"deleteButtonClass": function() {
		return Titles.userCanRemove(Meteor.userId(), this) ? "" : "hidden";
	}
});
