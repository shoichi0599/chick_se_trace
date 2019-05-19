$(function() {
    var $content = $('.content')
    var markdown = $content.text()
    if (markdown) {
        // Convert content(markdown) to html
        // marked() method is defined in the library "marked.js"
        marked.setOptions({breaks : true});
        var html = marked(markdown);
        // Put html converted from markdown(content)
        $content.html(html);
    }

    $('#toc_container').toc({
        'selectors': 'h1,h2,h3,h4,h5,h6,h7', //elements to use as headings
        'container': 'div.content', //element to find all selectors in
        'smoothScrolling': true, //enable or disable smooth scrolling on click
        'prefix': 'toc', //prefix for anchor tags and class names
        'anchorName': function(i, heading, prefix) { //custom function for anchor name
            return prefix+i;
        },
        'itemClass': function(i, heading, $heading, prefix) { // custom function for item class
          return $heading[0].tagName.toLowerCase();
        }
    });
});