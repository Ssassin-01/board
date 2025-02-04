import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import CommentPopup from './comment/CommentPopup';
import '../style/PostStyles.css';
import api from '../api/axiosInstance';

const PostCard = ({ post }) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const [isOverflowing, setIsOverflowing] = useState(false);
  const [showCommentsPopup, setShowCommentsPopup] = useState(false); // 댓글 팝업 상태
  const [showOptions, setShowOptions] = useState(false); // 수정/삭제 메뉴 상태
  const contentRef = useRef(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (contentRef.current) {
      setIsOverflowing(contentRef.current.scrollHeight > 300); // 본문 높이 초과 여부 확인
    }
  }, [post.content]);

  const handleNavigateToPost = () => {
    navigate(`/posts/${post.id}`);
  };

  const toggleCommentsPopup = () => {
    setShowCommentsPopup((prev) => !prev); // 댓글 팝업 상태 토글
  };

  const toggleOptions = () => {
    setShowOptions((prev) => !prev); // 수정/삭제 메뉴 토글
  };

  const handleEditPost = () => {
    navigate(`/posts/${post.id}/edit`);
  };

  const handleDeletePost = async () => {
    if (window.confirm('정말로 이 게시물을 삭제하시겠습니까?')) {
      try {
        await api.delete(`/posts/${post.id}`);
        alert('게시물이 삭제되었습니다.');
        window.location.reload(); // 삭제 후 페이지 새로고침
      } catch (error) {
        alert('게시물 삭제에 실패했습니다.');
      }
    }
  };

  return (
    <div className="post-card">
      {/* Header */}
      <div className="post-header">
        <img
          src="https://images.unsplash.com/photo-1738363436637-ee6f4a910715?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxmZWF0dXJlZC1waG90b3MtZmVlZHwyfHx8ZW58MHx8fHx8"
          alt="프로필"
          className="profile-img"
        />
        <span className="post-author">{post.author}</span>

        {/* ⋮ 메뉴 */}
        <div className="post-options">
          <button className="options-btn" onClick={toggleOptions}>
            ⋮
          </button>
          {showOptions && (
            <div className="options-dropdown">
              <button onClick={handleEditPost}>수정</button>
              <button onClick={handleDeletePost}>삭제</button>
            </div>
          )}
        </div>
      </div>

      {/* Content */}
      <p
        className={`post-content ${isExpanded ? 'expanded' : ''}`}
        ref={contentRef}
        onClick={handleNavigateToPost}
        style={{ cursor: 'pointer' }}
      >
        {post.content}
      </p>

      {/* '더보기' 버튼 */}
      {isOverflowing && !isExpanded && (
        <button className="more-btn" onClick={() => setIsExpanded(true)}>
          더보기
        </button>
      )}

      {/* Actions */}
      <div className="post-actions">
        <button className="like-btn">❤️ 좋아요</button>
        <button className="comment-btn" onClick={toggleCommentsPopup}>
          💬 댓글
        </button>
      </div>

      {/* Comments Popup */}
      {showCommentsPopup && (
        <CommentPopup postId={post.id} onClose={toggleCommentsPopup} />
      )}
    </div>
  );
};

export default PostCard;
