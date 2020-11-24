package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.TransactionTags;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.TagsRepository;
import KG.Neobis.FMS.dto.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionTagsService {

    private final TagsRepository tagsRepository;

    public TransactionTagsService(TagsRepository tagsRepository) {
        this.tagsRepository = tagsRepository;
    }

    public List<TransactionTags> getAllTags(){
        return tagsRepository.findAll();
    }

    public TransactionTags getTagByID(Long tagID){
        return tagsRepository.findById(tagID)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION," Не найден тэг с таким ID")));
    }

    public TransactionTags getTagByName(String name){
        return tagsRepository.findByName(name)
                .orElseGet(() ->{
                    return tagsRepository.save(new TransactionTags(name));
                });
    }

    public TransactionTags createNewTag (TransactionTags tag){
        return tagsRepository.save(tag);
    }

    public TransactionTags changeNameTag (TransactionTags newTag, Long tagID){
        return tagsRepository.findById(tagID)
                .map(tags -> {
                    tags.setName(newTag.getName());
                    return tagsRepository.save(tags);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION," Не найден тэг с таким ID")));
    }
}
