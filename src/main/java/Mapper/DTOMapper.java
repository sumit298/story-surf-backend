package Mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.storyapi.demo.Entity.AuthorApplication;
import com.storyapi.demo.Entity.Mood;
import com.storyapi.demo.Entity.Reflection;
import com.storyapi.demo.Entity.Reflection.ReflectionType;
import com.storyapi.demo.Entity.StoryDirectory.Story;
import com.storyapi.demo.Entity.StoryDirectory.Story.StoryLength;
import com.storyapi.demo.Entity.UserDirectory.User;
import com.storyapi.demo.dto.AuthorApplicationCreateDTO;
import com.storyapi.demo.dto.AuthorApplicationDTO;
import com.storyapi.demo.dto.MoodDTO;
import com.storyapi.demo.dto.ReflectionCreateDTO;
import com.storyapi.demo.dto.ReflectionDTO;
import com.storyapi.demo.dto.StoryCreateDTO;
import com.storyapi.demo.dto.StoryDTO;
import com.storyapi.demo.dto.UserDTO;
import com.storyapi.demo.dto.UserRegistrationDTO;

@Component
public class DTOMapper {
    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getRole(),
                user.getBio(),
                user.getAppliedForAuthor(),
                user.getCreatedAt());
    }

    public User toUser(UserRegistrationDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setBio(dto.getBio());

        return user;
    }

    public StoryDTO toStoryDTO(Story story) {
        if (story == null) {
            return null;
        }

        StoryDTO dto = new StoryDTO(
                story.getId(),
                story.getTitle(),
                story.getContent(),
                toUserDTO(story.getAuthor()), // Convert nested user
                story.getTags(),
                story.getContextTime(),
                story.getStatus(),
                story.getViews(),
                story.getLikes(),
                story.getCreatedAt(),
                story.getUpdatedAt()

        );
        
        if(story.getReflections() !=null){
            dto.setReflectionCount((long) story.getReflections().size());
        }
        
        return dto;

    }
    
    public Story toStory(StoryCreateDTO dto){
        if(dto == null){
            return null;
        }
        
        Story story = new Story();
        story.setTitle(dto.getTitle());
        story.setContent(dto.getContent());
        story.setTags(dto.getTags());
        
        try {
            story.setContextTime(StoryLength.valueOf(dto.getContextTime().toUpperCase()));    
        } catch (IllegalArgumentException e) {
            story.setContextTime(StoryLength.MEDIUM);
        }
        
        return story;
    }
    
    public List<StoryDTO> toStoryDTOList(List<Story> stories){
        if (stories == null) {
            return null;
        }
        
        return stories.stream().map(this::toStoryDTO).collect(Collectors.toList());    
    
    }
    
    public ReflectionDTO toReflectionDTO(Reflection reflection){
        if(reflection == null){
            return null;
        }
        
        return new ReflectionDTO(
            reflection.getId(),
            reflection.getStory().getId(),
            toUserDTO(reflection.getUser()),
            reflection.getType(),
            reflection.getContent(),
            reflection.getMoodReaction(),
            reflection.getCreatedAt(),
            reflection.getUpdatedAt()
        );
        
        
    }
    
    public Reflection toReflection(ReflectionCreateDTO dto){
        if(dto == null){
            return null;
        }  
        Reflection reflection = new Reflection();
        reflection.setContent(dto.getContent());
        reflection.setMoodReaction(dto.getMoodReaction());
        
        try {
            reflection.setType(ReflectionType.valueOf(dto.getType().toUpperCase()));    
        } catch (IllegalArgumentException e) {
            reflection.setType(ReflectionType.COMMENT);
        }
        
        return reflection;
    }
    
    public List<ReflectionDTO> toReflectionDTOList(List<Reflection> reflections){
        if(reflections == null){
            return null;
        }   
        
        return reflections.stream().map(this::toReflectionDTO).collect(Collectors.toList());
    }
    
    public AuthorApplicationDTO toAuthorApplicationDTO(AuthorApplication application){
        if(application == null ) return null;
        
        return new AuthorApplicationDTO(
            application.getId(),
            toUserDTO(application.getUser()),
            application.getStatus(),
            application.getReason(),
            application.getReviewedBy() != null ? toUserDTO(application.getReviewedBy()) : null,
            application.getCreatedAt(),
            application.getUpdatedAt()
        );
    }
    
    public AuthorApplication toAuthorApplication(AuthorApplicationCreateDTO dto){
        if(dto == null) return null;
        
        AuthorApplication application = new AuthorApplication();
        application.setReason(dto.getReason());
        
        return application;
    }
    
    public List<AuthorApplicationDTO> toAuthorApplicationDTOList(List<AuthorApplication> applications){
        if(applications == null) return null;
        
        return applications.stream().map(this::toAuthorApplicationDTO).collect(Collectors.toList());
    }
    
    public MoodDTO toMoodDTO(Mood mood){
        if(mood == null) return null;

        return new MoodDTO(
            mood.getId(),
            mood.getMoodName(),
            mood.getColorCode()
            
        );
    }
    
    public List<MoodDTO> toMoodDTOList(List<Mood> moods){
        if(moods == null) return null;  
        
        return moods.stream().map(this::toMoodDTO).collect(Collectors.toList());
    }
    
    
    // utility methods

    public void updateUserNameFromDTO(User user, UserRegistrationDTO dto){
        if(user == null || dto == null) return;
        
        if(dto.getName() != null){
            user.setName(dto.getName());
        }
        
        if(dto.getAge() != null){
            user.setAge(dto.getAge());
        }
        
        if(dto.getBio() != null){
            user.setBio(dto.getBio());
        }
    }
    
    public void updateStoryFromDTO(Story story, StoryCreateDTO dto){
         if (story == null || dto == null) return;
        
        if (dto.getTitle() != null) {
            story.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            story.setContent(dto.getContent());
        }
        if (dto.getTags() != null) {
            story.setTags(dto.getTags());
        }
        if (dto.getContextTime() != null) {
            try {
                story.setContextTime(StoryLength.valueOf(dto.getContextTime().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Keep existing context time if invalid
            }
        }
    }   
    
}
