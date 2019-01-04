$(function() {
  
  $.extend($.fn, {
    inputEmpty: function(c) {
      var $b = $(this);
      $b.each(function(d, e) {
        $(e).on("keyup", function() {
          var $t = $(this),
            val = $t.val(),
            $obj = $t.next(".inputEmpty");
          if (val == '' && $obj.length) {
            $obj.remove();
          };
          if(val != '' && !$obj.length) {
            var $cl = $('<div class="inputEmpty posAbs"><span class="icon block"></span></div>');
            $t.after($cl);
            $cl.on('click', function() {
              $t.val("").focus();
              $cl.remove();
            });
          };
        }).on("focusin", function() {
          var $t = $(this),
              val = $t.val().trim(),
            $obj = $t.next(".inputEmpty");
          if (val != '' && !$obj.length) {
            var $cl = $('<div class="inputEmpty posAbs"><span class="icon block"></span></div>');
            $t.after($cl);
            $cl.on('click', function() {
              $t.val("").focus();
              $cl.remove();
            });
          };
        }).on("focusout", function() {
          var $obj = $(this).next(".inputRemove");
          if($obj.length) {
            $obj.remove();
          };
        });
      });
      return this;
    }
  });

});