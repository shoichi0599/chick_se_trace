window.onload = function() {

    var $editor = $('.markdown-textarea');
    var convertMarkdownToHtml = function() {
        var $preview = $('.preview-cheat-sheet');
        var content = $editor.val()
        if (content) {
            // Convert content(markdown) to html
            // marked() method is defined in the library "marked.js"
            marked.setOptions({breaks : true});
            var html = marked(content);
            // Put html converted from markdown(content)
            $preview.html(html);

            // If the textarea is scrolled down, set the preview to the same position
            $editor.scroll(function() {
              $preview.scrollTop($(this).scrollTop());
            });
        } else {
            // When no content, put default text
            $preview.html('<h4>Markdown記法のヒント</h4>');
        }
    };

    // When any words are input/delete, convert markdown to HTML
    $editor.keyup(convertMarkdownToHtml)
}