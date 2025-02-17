import React, { useState } from 'react';
import api from '../../api/axiosInstance';
import '../../style/CommentStyles.css';

const CommentForm = ({ postId, onCommentAdded }) => {
  const [content, setContent] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!content.trim()) {
      alert('댓글 내용을 입력하세요.');
      return;
    }

    try {
      const response = await api.post(`/comments/${postId}/create`, {
        content,
      });
      onCommentAdded(response.data);
      setContent('');
    } catch (error) {
      alert(error.response?.data?.message || '댓글 작성에 실패했습니다.');
    }
  };

  return (
    <form className="comment-form" onSubmit={handleSubmit}>
      <textarea
        className="comment-input"
        placeholder="댓글을 입력하세요..."
        value={content}
        onChange={(e) => setContent(e.target.value)}
      />
      <button className="comment-submit" type="submit">
        작성하기
      </button>
    </form>
  );
};

export default CommentForm;
