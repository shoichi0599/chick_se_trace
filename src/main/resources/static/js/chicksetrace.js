function confirmDelete(){
  if(window.confirm('この投稿を削除してもよろしいですか？') == false) {
    return;
  }
  var deleteTag = getElementsByClassName('delete-content');
  window.location.href = deleteTag[0].href
}
