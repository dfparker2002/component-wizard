/**
 * %head-title
 *
 * @author %head-author-name
 * @version 1.0
 * @date-created %head-cur-date 
 * @desc %head-description
 * @global jQuery
 */

var EPAM = (function (epam, $) {

	// Augmenting shared objects and arrays
    epam.modules = epam.modules || {};

	function $jsFunctionName(opts){
		log('Component: "%title" was inited. (NOT CONFIGURATED)');

		var self = this;

		this.opts = $.extend(true, {}, this.defaults, opts);

		function _init(){

		}

		_init();
	}

	%jsFunctionName.prototype = {
		defaults: {
			selector: 'body'
		}
	};

	$.plugin('%jsFunctionName', %jsFunctionName);

	epam.modules.%jsFunctionName = %jsFunctionName;

	return epam;

})(EPAM || {}, jQuery);