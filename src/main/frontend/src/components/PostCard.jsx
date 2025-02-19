import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import CommentPopup from './comment/CommentPopup';
import '../style/PostStyles.css';
import api from '../api/axiosInstance';
import {
  likePost,
  unlikePost,
  checkLikedStatus,
  getLikeCount,
} from '../api/likeService';
import { useUser } from '../context/UserContext';

const PostCard = ({ post }) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const [isOverflowing, setIsOverflowing] = useState(false);
  const [showCommentsPopup, setShowCommentsPopup] = useState(false);
  const [showOptions, setShowOptions] = useState(false);
  const [isLiked, setIsLiked] = useState(false);
  const [likeCount, setLikeCount] = useState(post.likeCount || 0);
  const contentRef = useRef(null);
  const navigate = useNavigate();
  const { isLoggedIn } = useUser(); // ✅ 로그인 상태 가져오기

  useEffect(() => {
    if (contentRef.current) {
      setIsOverflowing(contentRef.current.scrollHeight > 300); // 본문 높이 초과 여부 확인
    }
  }, [post.content]);

  useEffect(() => {
    if (isLoggedIn) {
      checkLikedStatus(post.id).then(setIsLiked);
    }
    getLikeCount(post.id).then(setLikeCount);
  }, [post.id, isLoggedIn]);

  const handleNavigateToPost = () => {
    navigate(`/posts/${post.id}`);
  };

  const toggleCommentsPopup = () => {
    setShowCommentsPopup((prev) => !prev);
  };

  const toggleOptions = () => {
    setShowOptions((prev) => !prev);
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

  // ✅ 좋아요 버튼 클릭 핸들러
  const handleLikeToggle = async () => {
    if (!isLoggedIn) {
      alert('로그인 후 이용해주세요!');
      return;
    }

    try {
      if (isLiked) {
        await unlikePost(post.id);
        setIsLiked(false);
        setLikeCount((prev) => Math.max(0, prev - 1));
      } else {
        await likePost(post.id);
        setIsLiked(true);
        setLikeCount((prev) => prev + 1);
      }
    } catch (error) {
      console.error('좋아요 처리 중 오류 발생:', error);
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

      {/* body */}
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
        <button
          className={`likes-btn ${isLiked ? 'liked' : ''}`}
          onClick={handleLikeToggle}
        >
          {isLiked ? '❤️' : '🤍'} {likeCount}
        </button>
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
