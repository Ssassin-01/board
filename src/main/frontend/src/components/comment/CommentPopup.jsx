import React, { useState, useEffect } from 'react';
import api from '../../api/axiosInstance';
import CommentForm from './CommentForm';
import '../../style/CommentStyles.css';

const CommentPopup = ({ postId, onClose }) => {
  const [comments, setComments] = useState([]);
  const [editingCommentId, setEditingCommentId] = useState(null);
  const [updatedContent, setUpdatedContent] = useState('');
  const [expandedCommentId, setExpandedCommentId] = useState(null); // 대댓글 보기 상태
  const [newReply, setNewReply] = useState(''); // 대댓글 입력값

  useEffect(() => {
    const fetchComments = async () => {
      try {
        const response = await api.get(`/comments/${postId}`);
        setComments(response.data);
      } catch (error) {
        alert(error.response?.data?.message || '댓글 불러오기에 실패했습니다.');
      }
    };

    fetchComments();
  }, [postId]);

  const handleCommentAdded = (newComment) => {
    setComments((prev) => [newComment, ...prev]); // 새 댓글을 리스트 맨 앞에 추가
  };

  const handleDeleteComment = async (commentId) => {
    if (window.confirm('댓글을 삭제하시겠습니까?')) {
      try {
        await api.delete(`/comments/${commentId}`);
        setComments((prev) =>
          prev.filter((comment) => comment.id !== commentId)
        );
        alert('댓글이 삭제되었습니다.');
      } catch (error) {
        alert(error.response?.data?.message || '댓글 삭제에 실패했습니다.');
      }
    }
  };

  const handleEditComment = (commentId, content) => {
    setEditingCommentId(commentId);
    setUpdatedContent(content);
  };

  const handleUpdateComment = async () => {
    if (!updatedContent.trim()) {
      alert('수정할 내용을 입력하세요.');
      return;
    }

    try {
      const response = await api.put(`/comments/${editingCommentId}`, {
        content: updatedContent,
      });
      setComments((prev) =>
        prev.map((comment) =>
          comment.id === editingCommentId
            ? { ...comment, content: response.data.content }
            : comment
        )
      );
      setEditingCommentId(null); // 수정 상태 해제
      setUpdatedContent('');
      alert('댓글이 수정되었습니다.');
    } catch (error) {
      alert(error.response?.data?.message || '댓글 수정에 실패했습니다.');
    }
  };

  const handleReplyToggle = (commentId) => {
    setExpandedCommentId((prev) => (prev === commentId ? null : commentId));
  };

  const handleReplySubmit = async (parentId) => {
    if (!newReply.trim()) {
      alert('대댓글 내용을 입력하세요!');
      return;
    }

    try {
      const response = await api.post(`/comments/${postId}/create`, {
        content: newReply,
        parentId: parentId,
      });

      setComments((prev) =>
        prev.map((comment) =>
          comment.id === parentId
            ? {
                ...comment,
                replies: [response.data, ...(comment.replies || [])],
              }
            : comment
        )
      );
      setNewReply(''); // 입력값 초기화
    } catch (error) {
      alert(error.response?.data?.message || '대댓글 작성에 실패했습니다.');
    }
  };

  return (
    <div className="comment-popup">
      <div className="comment-popup-header">
        <h2 className="comment-title">댓글</h2>
        <button className="close-btn" onClick={onClose}>
          ❌
        </button>
      </div>
      <CommentForm postId={postId} onCommentAdded={handleCommentAdded} />
      <ul className="comment-list">
        {comments.map((comment) => (
          <li key={comment.id} className="comment-item">
            {/* 댓글 */}
            {editingCommentId === comment.id ? (
              <>
                <textarea
                  className="edit-input"
                  value={updatedContent}
                  onChange={(e) => setUpdatedContent(e.target.value)}
                />
                <button className="save-btn" onClick={handleUpdateComment}>
                  저장
                </button>
                <button
                  className="cancel-btn"
                  onClick={() => setEditingCommentId(null)}
                >
                  취소
                </button>
              </>
            ) : (
              <>
                <span>
                  <strong>{comment.author}</strong>: {comment.content}
                </span>
                <span className="comment-actions">
                  <button
                    className="edit-btn"
                    onClick={() =>
                      handleEditComment(comment.id, comment.content)
                    }
                  >
                    ✏️
                  </button>
                  <button
                    className="delete-btn"
                    onClick={() => handleDeleteComment(comment.id)}
                  >
                    🗑️
                  </button>
                  <button className="like-btn">❤️</button>
                  <button
                    className="reply-toggle-btn"
                    onClick={() => handleReplyToggle(comment.id)}
                  >
                    ↩️
                  </button>
                </span>
              </>
            )}
            {/* 대댓글 */}
            {expandedCommentId === comment.id && (
              <div className="reply-section">
                <ul className="reply-list">
                  {comment.replies &&
                    comment.replies.map((reply) => (
                      <li key={reply.id} className="reply-item">
                        <strong>{reply.author}</strong>: {reply.content}
                      </li>
                    ))}
                </ul>
                <div className="reply-form">
                  <textarea
                    value={newReply}
                    onChange={(e) => setNewReply(e.target.value)}
                    placeholder="대댓글 입력..."
                  />
                  <button
                    className="reply-submit"
                    onClick={() => handleReplySubmit(comment.id)}
                  >
                    작성
                  </button>
                </div>
              </div>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CommentPopup;
