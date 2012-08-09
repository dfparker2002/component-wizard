/**
 * %head-title
 *
 * @author %head-author-name
 * @version 1.0
 * @date-created %head-cur-date 
 * @desc %head-description
 * @global jQuery
 */

var %js-variable-name-main = (function (%js-variable-name, $) {

	// Augmenting shared objects and arrays
    %js-variable-name.modules = %js-variable-name.modules || {};

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

	%js-variable-name.modules.%jsFunctionName = %jsFunctionName;

	return %js-variable-name;

})(%js-variable-name-main || {}, jQuery);